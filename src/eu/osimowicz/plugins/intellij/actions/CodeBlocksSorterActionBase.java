package eu.osimowicz.plugins.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import eu.osimowicz.plugins.intellij.*;

import java.util.Comparator;

/**
 * Created by osimek1 on 2017-02-03.
 */
public abstract class CodeBlocksSorterActionBase extends AnAction {
    @Override
    public void update(final AnActionEvent actionEvent) {
        final Project project = actionEvent.getData(CommonDataKeys.PROJECT);
        final Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);

        //Set visibility only in case of existing project and editor
        actionEvent.getPresentation().setVisible((project != null && editor != null));
    }

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        IntellijEditor intellijEditor = new IntellijEditor(actionEvent);

        ILineParserHelpers parserHelpers = getLineParserHelpers(actionEvent);

        CodeBlockSorter codeBlockSorter = new CodeBlockSorter(parserHelpers);

        final String sortedCode = codeBlockSorter.getSortedCode(intellijEditor.extractLines(), getComparator(actionEvent));

        intellijEditor.replaceSelectedText(sortedCode);
    }

    protected ILineParserHelpers getLineParserHelpers(AnActionEvent actionEvent) {
        return new LineParserHelpers();
    }

    protected abstract Comparator<CodeBlock> getComparator(AnActionEvent actionEvent);
}
