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

    public int getIndentation() {
        return indentation;
    }

    public boolean hasStartBlockTag() {
        return hasStartBlockTag;
    }

    public void setHasStartBlockTag(boolean hasStartBlockTag) {
        this.hasStartBlockTag = hasStartBlockTag;
    }

    boolean isClosedBlock() {
        return hasStartBlockTag && hasEndBlockTag;
    }

    public boolean isCommentBlockOpened() {
        return isCommentBlockOpened;
    }

    public void setCommentBlockOpened(boolean commentBlockOpened) {
        isCommentBlockOpened = commentBlockOpened;
    }

    public boolean hasEndBlockTag() {
        return hasEndBlockTag;
    }

    @Override
    public void setHasEndBlockTag(boolean hasEndBlockTag) {
        this.hasEndBlockTag = hasEndBlockTag;
    }

    @NotNull
    @Override
    public List<String> getLines() {
        return lines;
    }

    public boolean hasSomeCodeLine() {
        return hasSomeCodeLine;
    }

    public void setHasSomeCodeLine(boolean hasSomeCodeLine) {
        this.hasSomeCodeLine = hasSomeCodeLine;
    }

    String getCode() {
        StringBuilder outputStringBuilder = new StringBuilder();
        for (String line : lines) {
            outputStringBuilder.append(line);
        }

        return outputStringBuilder.toString();
    }
}