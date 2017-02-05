package eu.osimowicz.plugins.intellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osimek1 on 2017-02-05.
 */
public class IntellijEditor implements IEditor {
    private final Document document;
    private final Project project;
    private final SelectionModel selectionModel;

    IntellijEditor(AnActionEvent actionEvent) {
        Editor editor = actionEvent.getRequiredData(CommonDataKeys.EDITOR);
        project = actionEvent.getRequiredData(CommonDataKeys.PROJECT);
        selectionModel = editor.getSelectionModel();

        this.document = editor.getDocument();
    }

    @Override
    public List<String> extractLines() {
        int startLine = getStartLine();
        int endLine = getEndLine();

        List<String> lines = new ArrayList<>(endLine - startLine);
        if (startLine >= endLine)
        {
            lines.add(selectionModel.getSelectedText());
            return lines;
        }

        for (int i = startLine; i <= endLine; i++)
        {
            String line = extractLine(i);
            lines.add(line);
        }

        return lines;
    }

    public void replaceSelectedText(String newText) {
        final int start = getSelectionStart();
        final int end = getSelectionEnd();

        Runnable runnable = () -> document.replaceString(start, end, newText);
        //Making the replacement
        WriteCommandAction.runWriteCommandAction(project, runnable);
        selectionModel.removeSelection();
    }

    private String extractLine(int lineNumber)
    {
        int lineSeparatorLength = document.getLineSeparatorLength(lineNumber);
        int startOffset = document.getLineStartOffset(lineNumber);
        int endOffset = document.getLineEndOffset(lineNumber) + lineSeparatorLength;

        String line = document.getCharsSequence().subSequence(startOffset, endOffset).toString();

        // If last line has no \n, add it one
        // This causes adding a \n at the end of file when sort is applied on whole file and the file does not end
        // with \n... This is fixed after.
        if (lineSeparatorLength == 0)
        {
            line += "\n";
        }

        return line;
    }

    private int getStartLine() {
        if (!selectionModel.hasSelection()) {
            return 0;
        }

        return document.getLineNumber(selectionModel.getSelectionStart());
    }

    private int getEndLine() {
        if (!selectionModel.hasSelection()) {
            return document.getLineCount() - 1;
        }

        int endLine = document.getLineNumber(selectionModel.getSelectionEnd());
        if (document.getLineStartOffset(endLine) == selectionModel.getSelectionEnd())
        {
            endLine--;
        }
        return endLine;
    }

    private int getSelectionStart() {
        int lineNumber = getStartLine();

        return document.getLineStartOffset(lineNumber);
    }

    private int getSelectionEnd() {
        int lineNumber = getEndLine();

        int lineSeparatorLength = document.getLineSeparatorLength(lineNumber);
        return document.getLineEndOffset(lineNumber) + lineSeparatorLength;
    }
}
