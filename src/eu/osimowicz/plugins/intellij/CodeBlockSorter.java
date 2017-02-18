package eu.osimowicz.plugins.intellij;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by osimek1 on 2017-02-03.
 */
public class CodeBlockSorter {
    private final ILineParserHelpers lineParserHelpers;

    public CodeBlockSorter(ILineParserHelpers lineParserHelpers) {
        this.lineParserHelpers = lineParserHelpers;
    }

    public String getSortedCode(List<String> lines, Comparator<CodeBlock> comparator) {
        List<CodeBlock> codeBlocks = parseCode(lines);

        codeBlocks.sort(comparator);

        return codeBlocksToText(codeBlocks);
    }

    private List<CodeBlock> parseCode(List<String> lines) {
        List<CodeBlock> codeBlocks = new ArrayList<>();

        CodeBlock currentCodeBlock = null;
        int indentation;

        for (String line : lines) {
            indentation = getIndentation(line);
            String lineWithoutIndentation = line.substring(indentation);

            if (isDifferentBlock(currentCodeBlock, indentation, lineWithoutIndentation)) {
                currentCodeBlock = new CodeBlock(indentation);
                codeBlocks.add(currentCodeBlock);
            }

            addLine(line, currentCodeBlock);
        }

        return codeBlocks;
    }

    private void addLine(String line, ICodeBlock codeBlock) {
        if (codeBlock.isCommentBlockOpened()) {
            if (lineParserHelpers.isEndBlockComment(line)) {
                codeBlock.setCommentBlockOpened(false);
            }

            codeBlock.getLines().add(line);
            return;
        }

        if (lineParserHelpers.isComment(line)) {
            codeBlock.getLines().add(line);
            return;
        }

        codeBlock.setCommentBlockOpened(codeBlock.isCommentBlockOpened() || lineParserHelpers.isStartBlockComment(line));

        if (!codeBlock.isCommentBlockOpened()) {
            codeBlock.setHasStartBlockTag(codeBlock.hasStartBlockTag() || lineParserHelpers.isStartCodeBlockTag(line));
            codeBlock.setHasEndBlockTag(codeBlock.hasEndBlockTag() || lineParserHelpers.isEndCodeBlockTag(line));
        }

        codeBlock.setHasSomeCodeLine(codeBlock.hasSomeCodeLine() || !lineParserHelpers.isComment(line, codeBlock));

        codeBlock.getLines().add(line);
    }

    private boolean isDifferentBlock(CodeBlock codeBlock, int indentation, String line) {
        if (codeBlock == null)
            return true;

        if (indentation > codeBlock.getIndentation()) {
            return false;
        }

        if (line.isEmpty()) {
            return false;
        }

        if (indentation == codeBlock.getIndentation()) {
            if (!codeBlock.hasSomeCodeLine()) {
                return false;
            }
            if (lineParserHelpers.isStartCodeBlockTag(line)) {
                return codeBlock.hasStartBlockTag();
            }

            if (lineParserHelpers.isStartBlockComment(line)) {
                return codeBlock.hasSomeCodeLine();
            }

            return !lineParserHelpers.isEndCodeBlockTag(line) && (!lineParserHelpers.isComment(line, codeBlock) || codeBlock.isClosedBlock());

        }

        return true;
    }

    private static int getIndentation(String line) {
        if (line.isEmpty()) {
            return 0;
        }

        for(int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return i;
            }
        }

        return line.length();
    }

    private static String codeBlocksToText(List<CodeBlock> codeBlocks) {
        StringBuilder outputStringBuilder = new StringBuilder();

        for (CodeBlock codeBlock : codeBlocks) {
            outputStringBuilder.append(codeBlock.getCode());
        }

        return outputStringBuilder.toString();
    }
}
