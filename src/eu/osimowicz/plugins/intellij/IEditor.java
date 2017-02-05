package eu.osimowicz.plugins.intellij;

import java.util.List;

/**
 * Created by osimek1 on 2017-02-05.
 */
interface IEditor {
    List<String> extractLines();
    void replaceSelectedText(String newText);
}
