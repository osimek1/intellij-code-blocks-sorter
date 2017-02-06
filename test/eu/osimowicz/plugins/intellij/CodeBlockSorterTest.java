package eu.osimowicz.plugins.intellij;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by osimek1 on 2017-02-05.
 */
public class CodeBlockSorterTest {
    @Test
    public void getSortedCodeSimpleCoffeeScriptCode() throws Exception {
        ArrayList codeLines = new ArrayList<String>() {{
            add("zModule: \r\n");
            add("    prop1: \"some text\"\r\n");
            add("    prop2: 2\r\n");
            add("        prop3_1: true\r\n");
            add("aModule: \"simple text\"\r\n");
            add("cModule: 1\r\n");
            add("bModule: [\r\n");
            add("    \"arr1\"\r\n");
            add("    \"arr2\"\r\n");
            add("]\r\n");
        }};

        String sortedCoffeeScriptCode = new StringBuilder()
            .append("aModule: \"simple text\"\r\n")
            .append("bModule: [\r\n")
            .append("    \"arr1\"\r\n")
            .append("    \"arr2\"\r\n")
            .append("]\r\n")
            .append("cModule: 1\r\n")
            .append("zModule: \r\n")
            .append("    prop1: \"some text\"\r\n")
            .append("    prop2: 2\r\n")
            .append("        prop3_1: true\r\n")
            .toString();

        CodeBlockSorter sut = new CodeBlockSorter();
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedCodeCoffeeScriptWithEmptyLines() throws Exception {
        ArrayList codeLines = new ArrayList<String>() {{
            add("zModule: \r\n");
            add("    prop1: \"some text\"\r\n");
            add("    prop2: 2\r\n");
            add("    prop3:\r\n");
            add("        prop3_1: true\r\n");
            add("\r\n");
            add("aModule: \"simple text\"\r\n");
            add("    \r\n");
            add("cModule: 1\r\n");
            add("bModule: [\r\n");
            add("    \"arr1\"\r\n");
            add("    \"arr2\"\r\n");
            add("]\r\n");
        }};

        String sortedCoffeeScriptCode = new StringBuilder()
            .append("aModule: \"simple text\"\r\n")
            .append("    \r\n")
            .append("bModule: [\r\n")
            .append("    \"arr1\"\r\n")
            .append("    \"arr2\"\r\n")
            .append("]\r\n")
            .append("cModule: 1\r\n")
            .append("zModule: \r\n")
            .append("    prop1: \"some text\"\r\n")
            .append("    prop2: 2\r\n")
            .append("    prop3:\r\n")
            .append("        prop3_1: true\r\n")
            .append("\r\n")
            .toString();

        CodeBlockSorter sut = new CodeBlockSorter();
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedCodeCoffeeScriptWithEmptyLinesAndJsDoc() throws Exception {
        ArrayList codeLines = new ArrayList<String>() {{
            add("###*\r\n");
            add(" # Some simple class desc zModule\r\n");
            add(" # @class zModule\r\n");
            add("###\r\n");
            add("zModule: \r\n");
            add("    ###*\r\n");
            add("     # Some simple property desc prop1\r\n");
            add("     # @prop prop1\r\n");
            add("    ###\r\n");
            add("    prop1: \"some text\"\r\n");
            add("    prop2: 2\r\n");
            add("    prop3:\r\n");
            add("        prop3_1: true\r\n");
            add("\r\n");
            add("###*\r\n");
            add(" # Some simple class desc aModule\r\n");
            add(" # @class aModule\r\n");
            add("###\r\n");
            add("aModule: \"simple text\"\r\n");
            add("    \r\n");
            add("###*\r\n");
            add(" # Some simple class desc cModule\r\n");
            add(" # @class cModule\r\n");
            add("###\r\n");
            add("cModule: 1\r\n");
            add("\r\n");
            add("###*\r\n");
            add(" # Some simple class desc bModule\r\n");
            add(" # @class bModule\r\n");
            add("###\r\n");
            add("bModule: [\r\n");
            add("    \"arr1\"\r\n");
            add("    \"arr2\"\r\n");
            add("]");
            add("\r\n");
        }};

        String sortedCoffeeScriptCode = new StringBuilder()
                .append("###*\r\n")
                .append(" # Some simple class desc aModule\r\n")
                .append(" # @class aModule\r\n")
                .append("###\r\n")
                .append("aModule: \"simple text\"\r\n")
                .append("    \r\n")
                .append("###*\r\n")
                .append(" # Some simple class desc bModule\r\n")
                .append(" # @class bModule\r\n")
                .append("###\r\n")
                .append("bModule: [\r\n")
                .append("    \"arr1\"\r\n")
                .append("    \"arr2\"\r\n")
                .append("]\r\n")
                .append("###*\r\n")
                .append(" # Some simple class desc cModule\r\n")
                .append(" # @class cModule\r\n")
                .append("###\r\n")
                .append("cModule: 1\r\n")
                .append("\r\n")
                .append("###*\r\n")
                .append(" # Some simple class desc zModule\r\n")
                .append(" # @class zModule\r\n")
                .append("###\r\n")
                .append("zModule: \r\n")
                .append("    ###*\r\n")
                .append("     # Some simple property desc prop1\r\n")
                .append("     # @prop prop1\r\n")
                .append("    ###\r\n")
                .append("    prop1: \"some text\"\r\n")
                .append("    prop2: 2\r\n")
                .append("    prop3:\r\n")
                .append("        prop3_1: true\r\n")
                .append("\r\n")
                .toString();

        CodeBlockSorter sut = new CodeBlockSorter();
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedJsonCodeWithEmptyLines() throws Exception {
        ArrayList codeLines = new ArrayList<String>() {{
            add("{\r\n");
            add("    \"zModule\": {\r\n");
            add("        \"prop1\": \"some text\"\r\n");
            add("        \"prop2\": 2\r\n");
            add("        \"prop3\": {\r\n");
            add("            \"prop3_1\": true\r\n");
            add("        }\r\n");
            add("    }\r\n");
            add("}\r\n");
            add("{\r\n");
            add("\r\n");
            add("    \"aModule\": \"simple text\"\r\n");
            add("}\r\n");
            add("    \r\n");
            add("{\r\n");
            add("    \"cModule\": 1\r\n");
            add("}\r\n");
            add("{\r\n");
            add("    \"bModule\": [\r\n");
            add("        \"arr1\"\r\n");
            add("        \"arr2\"\r\n");
            add("    ]\r\n");
            add("}\r\n");
            add("\r\n");
        }};

        String sortedCoffeeScriptCode = new StringBuilder()
                .append("{\r\n")
                .append("\r\n")
                .append("    \"aModule\": \"simple text\"\r\n")
                .append("}\r\n")
                .append("    \r\n")
                .append("{\r\n")
                .append("    \"bModule\": [\r\n")
                .append("        \"arr1\"\r\n")
                .append("        \"arr2\"\r\n")
                .append("    ]\r\n")
                .append("}\r\n")
                .append("\r\n")
                .append("{\r\n")
                .append("    \"cModule\": 1\r\n")
                .append("}\r\n")
                .append("{\r\n")
                .append("    \"zModule\": {\r\n")
                .append("        \"prop1\": \"some text\"\r\n")
                .append("        \"prop2\": 2\r\n")
                .append("        \"prop3\": {\r\n")
                .append("            \"prop3_1\": true\r\n")
                .append("        }\r\n")
                .append("    }\r\n")
                .append("}\r\n")
                .toString();

        CodeBlockSorter sut = new CodeBlockSorter();
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }
}

