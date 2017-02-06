package eu.osimowicz.plugins.intellij;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osimek1 on 2017-02-03.
 */
class CodeBlockSorter {
    String getSortedCode(List<String> lines) {
        List<CodeBlock> codeBlocks = parseCode(lines);

        codeBlocks.sort(CodeBlock.Comparator);

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

    private static void addLine(String line, ICodeBlock codeBlock) {
        if (codeBlock.isCommentBlockOpened()) {
            if (LineParserHelpers.isEndBlockComment(line)) {
                codeBlock.setCommentBlockOpened(false);
            }

            codeBlock.getLines().add(line);
            return;
        }

        if (LineParserHelpers.isComment(line)) {
            codeBlock.getLines().add(line);
            return;
        }

        codeBlock.setCommentBlockOpened(codeBlock.isCommentBlockOpened() || LineParserHelpers.isStartBlockComment(line));

        if (!codeBlock.isCommentBlockOpened()) {
            codeBlock.setHasStartBlockTag(codeBlock.hasStartBlockTag() || LineParserHelpers.isStartCodeBlockTag(line));
            codeBlock.setHasEndBlockTag(codeBlock.hasEndBlockTag() || LineParserHelpers.isEndCodeBlockTag(line));
        }

        codeBlock.setHasSomeCodeLine(codeBlock.hasSomeCodeLine() || !LineParserHelpers.isComment(line, codeBlock));

        codeBlock.getLines().add(line);
    }

    private static boolean isDifferentBlock(CodeBlock codeBlock, int indentation, String line) {
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
            if (LineParserHelpers.isStartCodeBlockTag(line)) {
                return codeBlock.hasStartBlockTag();
            }

            if (LineParserHelpers.isStartBlockComment(line)) {
                return codeBlock.hasSomeCodeLine();
            }

            if (LineParserHelpers.isEndCodeBlockTag(line)) {
                return false;
            }

            return !LineParserHelpers.isComment(line, codeBlock) || codeBlock.isClosedBlock();

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
