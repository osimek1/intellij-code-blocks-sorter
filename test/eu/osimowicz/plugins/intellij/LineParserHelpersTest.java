package eu.osimowicz.plugins.intellij;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by osimek1 on 2017-02-05.
 */
public class LineParserHelpersTest {
    private class CodeBlockMock implements ICodeBlock{
        private boolean isCommentBlockOpened = false;

        @Override
        public boolean isCommentBlockOpened() {
            return isCommentBlockOpened;
        }

        @Override
        public List<String> getLines() {
            return new ArrayList<>();
        }

        @Override
        public int getIndentation() {
            return 0;
        }

        public void setCommentBlockOpened(boolean commentBlockOpened) {
            isCommentBlockOpened = commentBlockOpened;
        }
    }

    private List<String> addWhiteSpacesToCodeLines(String[] codeLines) {
        String[] whiteSpaces = {
                "\t",
                "    "
        };

        List<String> response = new ArrayList<>();

        for (String whiteSpace : whiteSpaces) {
            for (String codeLine : codeLines) {
                response.add(whiteSpace.concat(codeLine));
                response.add(codeLine);
                response.add(codeLine.concat(whiteSpace));
                response.add(codeLine.concat("\r\n"));
                response.add(codeLine.concat(whiteSpace).concat("\r\n"));
            }
        }

        return response;
    }

    @Test
    public void isStartCodeBlockTag() throws Exception {
        List<String> startBlockCodeTags = addWhiteSpacesToCodeLines(new String[]{
                "{",
                "(",
                "<",
                "[",
                "{ someText",
                "( someText",
                "< someText",
                "[ someText",
        });

        for (String blockTag : startBlockCodeTags) {
            assertTrue(
                    "\"" + blockTag +"\" should be a start code block tag",
                    LineParserHelpers.isStartCodeBlockTag(blockTag)
            );
        }
    }

    @Test
    public void shouldReturnFalseForInvalidBlockTag() throws Exception {
        List<String> linesWithEndBlockTags = addWhiteSpacesToCodeLines(new String[]{
                "someText {",
                "someText (",
                "someText [",
                "someText <",
                "someText { and other text",
                "someText ( and other text",
                "someText [ and other text",
                "someText < and other text",
                "]",
                "}",
                ")"
        });
        for (String blockTag : linesWithEndBlockTags) {
            assertFalse(
                    "\"" + blockTag +"\" should not be a start code block tag",
                    LineParserHelpers.isStartCodeBlockTag(blockTag)
            );
        }
    }

    @Test
    public void isEndCodeBlockTag() throws Exception {
        List<String> endBlockCodeTags = addWhiteSpacesToCodeLines(new String[]{
                "}",
                ")",
                ">",
                "]",
                "someText }",
                "someText )",
                "someText >",
                "someText ]",
        });

        for (String blockTag : endBlockCodeTags) {
            assertTrue(
                    "\"" + blockTag +"\" should be a end code block tag",
                    LineParserHelpers.isEndCodeBlockTag(blockTag)
            );
        }
    }

    @Test
    public void isComment() throws Exception {
        List<String> validComments = addWhiteSpacesToCodeLines(new String[]{
                "# some comment",
                "// some comment",
                "/* some comment*/",
                "/**/",
                "### some text ###",
                "######"
        });

        for (String comment : validComments) {
            assertTrue("\"" + comment + "\" should be valid comment", LineParserHelpers.isComment(comment));
        }

        List<String> invalidComments = addWhiteSpacesToCodeLines(new String[]{
                "/* some comment",
                "some comment*/",
                "some text //",
                "some text #"
        });

        for (String comment : invalidComments) {
            assertFalse("\"" + comment + "\" should be invalid comment", LineParserHelpers.isComment(comment));
        }
    }

    @Test
    public void isBlockCommentWhenCodeBlockHasBlockCommentOpened() throws Exception {
        CodeBlockMock codeBlock = new CodeBlockMock();
        codeBlock.setCommentBlockOpened(false);

        assertFalse(
                "Should return false if not a comment and no block comment",
                LineParserHelpers.isComment("some text", codeBlock)
        );

        assertFalse(
                "Should return false if end block comment but there is no start comment",
                LineParserHelpers.isComment("some text */", codeBlock)
        );

        assertTrue(
                "Should return true if inline comment, no block comment",
                LineParserHelpers.isComment("// some text", codeBlock)
        );

        assertTrue(
                "Should return true if start block comment",
                LineParserHelpers.isComment("/* some text", codeBlock)
        );

        codeBlock.setCommentBlockOpened(true);
        assertTrue(
                "Should return true if block comment started and sample text passed",
                LineParserHelpers.isComment("some text", codeBlock)
        );

        assertTrue(
                "Should return if block comment started and inline comment passed",
                LineParserHelpers.isComment("// some text", codeBlock)
        );

        assertTrue(
                "Should return if block comment started and end block comment passed",
                LineParserHelpers.isComment("some text /*", codeBlock)
        );
    }

    @Test
    public void isEndBlockComment() throws Exception {
        List<String> validEndBlockCommentTags = addWhiteSpacesToCodeLines(new String[]{
                "some text */",
                "some text ###"
        });

        for (String commentTag : validEndBlockCommentTags) {
            assertTrue(
                    "\"" + commentTag + "\" should be valid end block comment tag",
                    LineParserHelpers.isEndBlockComment(commentTag)
            );
        }


        List<String> invalidEndBlockCommentTags = addWhiteSpacesToCodeLines(new String[]{
                "/* some text",
                "# some text",
                "// some text",
                "some text"
        });

        for (String commentTag : invalidEndBlockCommentTags) {
            assertFalse(
                    "\"" + commentTag + "\" should be invalid end block comment tag",
                    LineParserHelpers.isEndBlockComment(commentTag)
            );
        }
    }

    @Test
    public void isStartBlockComment() throws Exception {
        List<String> validStartBlockCommentTags = addWhiteSpacesToCodeLines(new String[]{
                "/* some text ",
                "/*",
                "### some text ",
                "###",
                "###*"
        });

        for (String commentTag : validStartBlockCommentTags) {
            assertTrue(
                    "\"" + commentTag + "\" should be valid start block comment tag",
                    LineParserHelpers.isStartBlockComment(commentTag)
            );
        }


        List<String> invalidStartBlockCommentTags = addWhiteSpacesToCodeLines(new String[]{
                "some text */",
                "# some text ",
                "// some text",
                "some text"
        });

        for (String commentTag : invalidStartBlockCommentTags) {
            assertFalse(
                    "\"" + commentTag + "\" should be invalid start block comment tag",
                    LineParserHelpers.isStartBlockComment(commentTag)
            );
        }
    }
}