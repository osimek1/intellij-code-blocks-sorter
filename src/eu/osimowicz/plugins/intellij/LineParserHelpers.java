package eu.osimowicz.plugins.intellij;

import java.util.regex.Pattern;

/**
 * Created by osimek1 on 2017-02-05.
 */
class LineParserHelpers {
    private static final Pattern startBlockTags = Pattern.compile("(\\s+)*(\\[|\\{|\\(|<)");
    private static final Pattern endBlockTags = Pattern.compile("[[\\s]*[\\w]*]*(]|}|\\)|>)(\\s+)*$");
    private static final Pattern inlineCommentTags = Pattern.compile("(\\s)*(//|(#(\\s*.*))|((/\\*)(\\s*.*)*(\\*/)))");
    private static final Pattern startBlockCommentTags = Pattern.compile("(\\s)*(/\\*|###)");
    private static final Pattern endBlockCommentTags = Pattern.compile("[[\\s+]*[\\w]*]*(\\*/|###)");

    static boolean isStartCodeBlockTag(String line) {
        return startBlockTags.matcher(line).lookingAt();
    }

    static boolean isEndCodeBlockTag(String line) {
        return endBlockTags.matcher(line).lookingAt();
    }

    // @todo "### some text" should not be a valid inline comment
    static boolean isComment(String line) {
        return inlineCommentTags.matcher(line).lookingAt();
    }

    static boolean isComment(String line, ICodeBlock codeBlock) {
        return isComment(line) || codeBlock.isCommentBlockOpened() && !isEndBlockComment(line) || isStartBlockComment(line);
    }

    static boolean isEndBlockComment(String line) {
        return endBlockCommentTags.matcher(line).lookingAt();
    }
    static boolean isStartBlockComment(String line) {
        return startBlockCommentTags.matcher(line).lookingAt();
    }
}
