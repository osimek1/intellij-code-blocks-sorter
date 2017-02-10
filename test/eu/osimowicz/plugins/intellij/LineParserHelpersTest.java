package eu.osimowicz.plugins.intellij;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by osimek1 on 2017-02-05.
 */
public class LineParserHelpersTest {
    private LineParserHelpers lineParserHelpers;

    private class CodeBlockMock implements ICodeBlock{
        private boolean isCommentBlockOpened = false;
        private boolean hasStartBlockTag = false;
        private boolean hasEndBlockTag = false;
        private boolean hasSomeCodeLine = false;

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

        @Override
        public boolean hasStartBlockTag() {
            return hasStartBlockTag;
        }

        @Override
        public void setHasStartBlockTag(boolean hasStartBlockTag) {
            this.hasStartBlockTag = hasStartBlockTag;
        }

        @Override
        public boolean hasEndBlockTag() {
            return hasEndBlockTag;
        }

        @Override
        public void setHasEndBlockTag(boolean hasEndBlockTag) {
            this.hasEndBlockTag = hasEndBlockTag;
        }

        @Override
        public boolean hasSomeCodeLine() {
            return hasSomeCodeLine;
        }

        @Override
        public void setHasSomeCodeLine(boolean hasSomeCodeLine) {
            this.hasSomeCodeLine = hasSomeCodeLine;
        }

        public void setCommentBlockOpened(boolean commentBlockOpened) {
            isCommentBlockOpened = commentBlockOpened;
        }
    }

    @Before
    public void setUp() throws Exception {
        lineParserHelpers = new LineParserHelpers();
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
                    lineParserHelpers.isStartCodeBlockTag(blockTag)
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
                    lineParserHelpers.isStartCodeBlockTag(blockTag)
            );
        }
    }

    @Test
    public void isEndCodeBlockTag() throws Exception {
        List<String> endBlockCodeTags = addWhiteSpacesToCodeLines(new String[]{
                "}",
                "},",
                "};",
                ")",
                "),",
                ");",
                ">",
                ">,",
                ">;",
                "]",
                "],",
                "];",
                "someText }",
                "someText },",
                "someText };",
                "someText )",
                "someText ),",
                "someText );",
                "someText >",
                "someText >,",
                "someText >;",
                "someText ]",
                "someText ],",
                "someText ];",
        });

        for (String blockTag : endBlockCodeTags) {
            assertTrue(
                    "\"" + blockTag +"\" should be a end code block tag",
                    lineParserHelpers.isEndCodeBlockTag(blockTag)
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
            assertTrue("\"" + comment + "\" should be valid comment", lineParserHelpers.isComment(comment));
        }

        List<String> invalidComments = addWhiteSpacesToCodeLines(new String[]{
                "/* some comment",
                "some comment*/",
                "some text //",
                "some text #"
        });

        for (String comment : invalidComments) {
            assertFalse("\"" + comment + "\" should be invalid comment", lineParserHelpers.isComment(comment));
        }
    }

    @Test
    public void isBlockCommentWhenCodeBlockHasBlockCommentOpened() throws Exception {
        CodeBlockMock codeBlock = new CodeBlockMock();
        codeBlock.setCommentBlockOpened(false);

        assertFalse(
                "Should return false if not a comment and no block comment",
                lineParserHelpers.isComment("some text", codeBlock)
        );

        assertFalse(
                "Should return false if end block comment but there is no start comment",
                lineParserHelpers.isComment("some text */", codeBlock)
        );

        assertTrue(
                "Should return true if inline comment, no block comment",
                lineParserHelpers.isComment("// some text", codeBlock)
        );

        assertTrue(
                "Should return true if start block comment",
                lineParserHelpers.isComment("/* some text", codeBlock)
        );

        codeBlock.setCommentBlockOpened(true);
        assertTrue(
                "Should return true if block comment started and sample text passed",
                lineParserHelpers.isComment("some text", codeBlock)
        );

        assertTrue(
                "Should return if block comment started and inline comment passed",
                lineParserHelpers.isComment("// some text", codeBlock)
        );

        assertTrue(
                "Should return if block comment started and end block comment passed",
                lineParserHelpers.isComment("some text /*", codeBlock)
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
                    lineParserHelpers.isEndBlockComment(commentTag)
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
                    lineParserHelpers.isEndBlockComment(commentTag)
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
                    lineParserHelpers.isStartBlockComment(commentTag)
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
                    lineParserHelpers.isStartBlockComment(commentTag)
            );
        }
    }
}