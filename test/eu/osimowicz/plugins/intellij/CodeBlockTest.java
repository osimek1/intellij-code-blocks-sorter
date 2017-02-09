package eu.osimowicz.plugins.intellij;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gosimowi on 2017-02-07.
 */
public class CodeBlockTest {
    @Test
    public void CodeBlockInvalidIndentation() throws Exception {
        boolean thrown = false;

        try {
            new CodeBlock(-1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }

        assertTrue("IllegalArgumentException should be thrown if indentation is less then 0",thrown);
    }

    @Test
    public void CodeBlockValidIndentation() throws Exception {
        boolean thrown = false;

        try {
            new CodeBlock(0);
            new CodeBlock(1);
        } catch (Exception e) {
            thrown = true;
        }

        assertFalse("No exception should be thrown if indentation is great then or equal 0", thrown);
    }
}