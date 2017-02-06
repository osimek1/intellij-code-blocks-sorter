package eu.osimowicz.plugins.intellij;

import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.Line;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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

    void addLine(String line) {
        if (addCommentLine(line)) return;

        // @TODO extract this logic
        isCommentBlockOpened = isCommentBlockOpened || LineParserHelpers.isStartBlockComment(line);

        if (!isCommentBlockOpened) {
            hasStartBlockTag = hasStartBlockTag || LineParserHelpers.isStartCodeBlockTag(line);
            hasEndBlockTag = hasEndBlockTag || LineParserHelpers.isEndCodeBlockTag(line);
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

    boolean hasStartBlockTag() {
        return hasStartBlockTag;
    }

    boolean isClosedBlock() {
        return hasStartBlockTag && hasEndBlockTag;
    }

    public boolean isCommentBlockOpened() {
        return isCommentBlockOpened;
    }

    @Override
    public final List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    boolean hasSomeCodeLine() {
        return hasSomeCodeLine;
    }

    static final Comparator<CodeBlock> Comparator = java.util.Comparator.comparing(LineParserHelpers::getFirstCodeLine);

    String getCode() {
        StringBuilder outputStringBuilder = new StringBuilder();
        for (String line : lines) {
            outputStringBuilder.append(line);
        }

        return outputStringBuilder.toString();
    }
}