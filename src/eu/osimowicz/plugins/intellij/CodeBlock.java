package eu.osimowicz.plugins.intellij;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by osimek1 on 2017-02-03.
 */
class CodeBlock implements ICodeBlock {
    private List<String> lines;

    private boolean hasStartBlockTag = false;
    private boolean hasEndBlockTag = false;
    private boolean hasSomeCodeLine = false;
    private boolean isCommentBlockOpened = false;
    private int indentation;

    CodeBlock(int indentation) {
        if (indentation < 0) {
            throw new IllegalArgumentException("Indentation cannot be negative");
        }
        this.indentation = indentation;
        lines = new ArrayList<>();
    }

    public void addLine(String line) {
        if (addCommentLine(line)) return;

        isCommentBlockOpened = LineParserHelpers.isStartBlockComment(line);
        if (!hasStartBlockTag && isCommentBlockOpened) {
            hasStartBlockTag = true;
            hasEndBlockTag = false;
        }

        if (!isCommentBlockOpened && !hasEndBlockTag && LineParserHelpers.isEndCodeBlockTag(line)) {
            hasEndBlockTag = true;
        }

        hasSomeCodeLine = hasSomeCodeLine || !LineParserHelpers.isComment(line, this);

        lines.add(line);
    }

    private boolean addCommentLine(String line) {
        if (isCommentBlockOpened) {
            if (LineParserHelpers.isEndBlockComment(line)) {
                isCommentBlockOpened = false;
            }

            lines.add(line);
            return true;
        }

        if (LineParserHelpers.isComment(line)) {
            lines.add(line);
            return true;
        }
        return false;
    }

    public int getIndentation() {
        return indentation;
    }

    public boolean hasStartBlockTag() {
        return hasStartBlockTag;
    }

    public boolean isClosedBlock() {
        return hasStartBlockTag && hasEndBlockTag;
    }

    public boolean isCommentBlockOpened() {
        return isCommentBlockOpened;
    }

    public boolean hasSomeCodeLine() {
        return hasSomeCodeLine;
    }

    private String getFirstCodeLine() {
        String lineWithoutWhiteSpaces;
        boolean isBlockComment = false;

        for (String line : lines) {
            lineWithoutWhiteSpaces = skipIndentation(line);
            if (lineWithoutWhiteSpaces.isEmpty()) {
                continue;
            }

            if (LineParserHelpers.isComment(lineWithoutWhiteSpaces)) {
                continue;
            }

            if (!isBlockComment && LineParserHelpers.isStartBlockComment(lineWithoutWhiteSpaces)) {
                isBlockComment = true;
            }

            if (isBlockComment && LineParserHelpers.isEndBlockComment(lineWithoutWhiteSpaces)) {
                isBlockComment = false;
                continue;
            }

            if (LineParserHelpers.isStartCodeBlockTag(lineWithoutWhiteSpaces)) {
                continue;
            }

            return lineWithoutWhiteSpaces;
        }
        return "";
    }

    @NotNull
    private String skipIndentation(String line) {
        return line.substring(indentation);
    }

    static final Comparator<CodeBlock> Comparator = (o1, o2) -> o1.getFirstCodeLine().compareTo(o2.getFirstCodeLine());

    String getCode() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter, true);
        for (String line : lines) {
            writer.println(line);
        }

        return stringWriter.toString();
    }
}