package eu.osimowicz.plugins.intellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import eu.osimowicz.plugins.intellij.CodeBlock;
import eu.osimowicz.plugins.intellij.ILineParserHelpers;

import java.util.Comparator;

/**
 * Created by osimek1 on 2017-02-18.
 */
public class SortCodeBlockCaseSensitiveAction extends CodeBlocksSorterActionBase {
    @Override
    protected Comparator<CodeBlock> getComparator(AnActionEvent actionEvent) {
        ILineParserHelpers lineParserHelpers = getLineParserHelpers(actionEvent);

        final Comparator<CodeBlock> comparator = java.util.Comparator.comparing((codeBlock) ->
                lineParserHelpers.getFirstCodeLine(codeBlock)
        );

        return comparator;
    }
}
