package eu.osimowicz.plugins.intellij;

import java.util.List;

/**
 * Created by osimek1 on 2017-02-06.
 */
interface ICodeBlock {
    boolean isCommentBlockOpened();
    List<String> getLines();
    int getIndentation();
}
