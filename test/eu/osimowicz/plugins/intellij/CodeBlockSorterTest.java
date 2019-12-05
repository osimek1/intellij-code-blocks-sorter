package eu.osimowicz.plugins.intellij;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.*;

/**
 * Created by osimek1 on 2017-02-05.
 */
public class CodeBlockSorterTest {
    private CodeBlockSorter sut;
    private Comparator<CodeBlock> comparator;

    @Before
    public void setUp() throws Exception {
        ILineParserHelpers lineParserHelpers = new LineParserHelpers();
        // @TODO do not use LineParserHelpers - write some mock
        sut = new CodeBlockSorter(lineParserHelpers);
        comparator = java.util.Comparator.comparing((codeBlock) ->
                lineParserHelpers.getFirstCodeLine(codeBlock).toLowerCase()
        );
    }

    @Test
    public void getSortedCodeSimpleCoffeeScriptCode() throws Exception {
        ArrayList<String> codeLines = new ArrayList<String>() {{
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

        String sortedText = sut.getSortedCode(codeLines, comparator);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedCodeCoffeeScriptWithEmptyLines() throws Exception {
        ArrayList<String> codeLines = new ArrayList<String>() {{
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

        String sortedText = sut.getSortedCode(codeLines, comparator);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedCodeCoffeeScriptWithEmptyLinesAndJsDoc() throws Exception {
        ArrayList<String> codeLines = new ArrayList<String>() {{
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

        String sortedText = sut.getSortedCode(codeLines, comparator);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedJsonCodeWithEmptyLines() throws Exception {
        ArrayList<String> codeLines = new ArrayList<String>() {{
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

        String sortedCode = new StringBuilder()
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

        String sortedText = sut.getSortedCode(codeLines, comparator);
        assertEquals("Code should be sorted by first level modules", sortedCode, sortedText);
    }

    @Test
    public void getSortedJson() {
        ArrayList<String> codeLines = new ArrayList<String>() {{
            add("    \"tags\": {\n");
            add("        \"allowUnknownTags\": true\n");
            add("    },\n");
            add("    \"source\": {\n");
            add("        \"includePattern\": \".+\\\\.js(doc)?$\",\n");
            add("        \"excludePattern\": \"(^|\\\\/|\\\\\\\\)_[^\\\\/\\\\\\\\]*$\"\n");
            add("    },\n");
            add("    \"plugins\": [\"plugins/markdown\"],\n");
            add("    \"templates\": {\n");
            add("        \"cleverLinks\": false,\n");
            add("        \"monospaceLinks\": true,\n");
            add("        \"default\": {\n");
            add("            \"outputSourceFiles\": false\n");
            add("        }\n");
            add("    }\n");
        }};

        String sortedCode = "" +
                "    \"plugins\": [\"plugins/markdown\"],\n" +
                "    \"source\": {\n" +
                "        \"includePattern\": \".+\\\\.js(doc)?$\",\n" +
                "        \"excludePattern\": \"(^|\\\\/|\\\\\\\\)_[^\\\\/\\\\\\\\]*$\"\n" +
                "    },\n" +
                "    \"tags\": {\n" +
                "        \"allowUnknownTags\": true\n" +
                "    },\n" +
                "    \"templates\": {\n" +
                "        \"cleverLinks\": false,\n" +
                "        \"monospaceLinks\": true,\n" +
                "        \"default\": {\n" +
                "            \"outputSourceFiles\": false\n" +
                "        }\n" +
                "    }\n";

        String sortedText = sut.getSortedCode(codeLines, comparator);
        assertEquals("Code should be sorted by first level modules", sortedCode, sortedText);
    }

    @Test
    public void getSortedCodeWithAnnotations() {
        ArrayList<String> codeLines = new ArrayList<String>() {{
            add("private CodeBlockSorter sut;\n");
            add("private Comparator<CodeBlock> comparator;\n");
            add("\n");
            add("@Before\n");
            add("public void setUp() throws Exception {\n");
            add("    ILineParserHelpers lineParserHelpers = new LineParserHelpers();\n");
            add("    // @TODO do not use LineParserHelpers - write some mock\n");
            add("    sut = new CodeBlockSorter(lineParserHelpers);\n");
            add("    comparator = java.util.Comparator.comparing((codeBlock) ->\n");
            add("        lineParserHelpers.getFirstCodeLine(codeBlock).toLowerCase()\n");
            add("    );\n");
            add("}\n");
            add("\n");
            add("@Test\n");
            add("public void getSortedCodeSimpleCoffeeScriptCode() throws Exception {\n");
            add("    string sampleStringVariable = \"test\";\n");
            add("}\n");
        }};

        String sortedCode = "" +
            "private Comparator<CodeBlock> comparator;\n" +
            "private CodeBlockSorter sut;\n" +
            "\n" +
            "@Test\n" +
            "public void getSortedCodeSimpleCoffeeScriptCode() throws Exception {\n" +
            "    string sampleStringVariable = \"test\";\n" +
            "}\n" +
            "\n" +
            "@Before\n" +
            "public void setUp() throws Exception {\n" +
            "    ILineParserHelpers lineParserHelpers = new LineParserHelpers();\n" +
            "    // @TODO do not use LineParserHelpers - write some mock\n" +
            "    sut = new CodeBlockSorter(lineParserHelpers);\n" +
            "    comparator = java.util.Comparator.comparing((codeBlock) ->\n" +
            "        lineParserHelpers.getFirstCodeLine(codeBlock).toLowerCase()\n" +
            "    );\n" +
            "}\n" +
            "\n"
            ;

        String sortedText = sut.getSortedCode(codeLines, comparator);
        assertEquals("Code should be sorted by first level modules", sortedCode, sortedText);
    }
}

