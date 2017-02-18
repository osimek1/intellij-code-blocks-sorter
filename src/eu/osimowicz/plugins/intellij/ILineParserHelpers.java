package eu.osimowicz.plugins.intellij;

/**
 * Created by gosimowi on 2017-02-06.z
 */
public interface ILineParserHelpers {
    boolean isStartCodeBlockTag(String line);
    boolean isEndCodeBlockTag(String line);
    boolean isComment(String line);
    boolean isComment(String line, ICodeBlock codeBlock);
    boolean isEndBlockComment(String line);
    boolean isStartBlockComment(String line);
    String getFirstCodeLine(ICodeBlock codeBlock);
}
