package eu.osimowicz.plugins.intellij;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by osimek1 on 2017-02-05.
 */
class LineParserHelpers implements ILineParserHelpers {
    private static final Pattern startBlockTags = Pattern.compile("(\\s+)*(\\[|\\{|\\(|<)");
    private static final Pattern endBlockTags = Pattern.compile("[[\\s]*[\\w]*]*(]|}|\\)|>)[[\\s]*[\\w,\\.\\;]*]*$");
    private static final Pattern inlineCommentTags = Pattern.compile("(\\s)*(//|(#(\\s*.*))|((/\\*)(\\s*.*)*(\\*/)))");
    private static final Pattern startBlockCommentTags = Pattern.compile("(\\s)*(/\\*|###)");
    private static final Pattern endBlockCommentTags = Pattern.compile("[[\\s+]*[\\w]*]*(\\*/|###)");

    public boolean isStartCodeBlockTag(String line) {
        return startBlockTags.matcher(line).lookingAt();
    }

    public boolean isEndCodeBlockTag(String line) {
        return endBlockTags.matcher(line).lookingAt();
    }

    // @todo "### some text" should not be a valid inline comment
    public boolean isComment(String line) {
        return inlineCommentTags.matcher(line).lookingAt();
    }

    public boolean isComment(String line, ICodeBlock codeBlock) {
        return isComment(line) || codeBlock.isCommentBlockOpened() && !isEndBlockComment(line) || isStartBlockComment(line);
    }

    public boolean isEndBlockComment(String line) {
        return endBlockCommentTags.matcher(line).lookingAt();
    }

    public boolean isStartBlockComment(String line) {
        return startBlockCommentTags.matcher(line).lookingAt();
    }

    public String getFirstCodeLine(ICodeBlock codeBlock) {
        String lineWithoutWhiteSpaces;
        boolean isBlockComment = false;

        List<String> lines = codeBlock.getLines();

        for (String line : lines) {
            lineWithoutWhiteSpaces = line.substring(codeBlock.getIndentation());
            if (lineWithoutWhiteSpaces.isEmpty()) {
                continue;
            }

            if (isComment(lineWithoutWhiteSpaces)) {
                continue;
            }

            if (!isBlockComment && isStartBlockComment(lineWithoutWhiteSpaces)) {
                isBlockComment = true;
            }

            if (isBlockComment && isEndBlockComment(lineWithoutWhiteSpaces)) {
                isBlockComment = false;
                continue;
            }

            if (isStartCodeBlockTag(lineWithoutWhiteSpaces)) {
                continue;
            }

            return lineWithoutWhiteSpaces;
        }
        return "";
    }
}
