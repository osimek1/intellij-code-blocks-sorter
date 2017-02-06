package eu.osimowicz.plugins.intellij;

import java.util.List;

/**
 * Created by osimek1 on 2017-02-06.
 */
interface ICodeBlock {
    boolean isCommentBlockOpened();
    void setCommentBlockOpened(boolean commentBlockOpened);
    List<String> getLines();
    int getIndentation();
    boolean hasStartBlockTag();
    void setHasStartBlockTag(boolean hasStartBlockTag);
    boolean hasEndBlockTag();
    void setHasEndBlockTag(boolean hasEndBlockTag);
    boolean hasSomeCodeLine();
    void setHasSomeCodeLine(boolean hasSomeCodeLine);
}
