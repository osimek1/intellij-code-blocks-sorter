package eu.osimowicz.plugins.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * Created by osimek1 on 2017-02-03.
 */
public class CodeBlocksSorterAction extends AnAction {
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
        CodeBlockSorter codeBlockSorter = new CodeBlockSorter();

        final String sortedCode = codeBlockSorter.getSortedCode(intellijEditor.extractLines());

        intellijEditor.replaceSelectedText(sortedCode);
    }
}
