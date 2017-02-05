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
        String coffeeScriptCode = "" +
                "zModule: " + System.getProperty("line.separator") +
                "    prop1: \"some text\"" + System.getProperty("line.separator") +
                "    prop2: 2" + System.getProperty("line.separator") +
                "    prop3:" + System.getProperty("line.separator") +
                "        prop3_1: true" + System.getProperty("line.separator") +
                "aModule: \"simple text\"" + System.getProperty("line.separator") +
                "cModule: 1" + System.getProperty("line.separator") +
                "bModule: [" + System.getProperty("line.separator") +
                "    \"arr1\"" + System.getProperty("line.separator") +
                "    \"arr2\"" + System.getProperty("line.separator") +
                "]" + System.getProperty("line.separator");

        String sortedCoffeeScriptCode = "" +
                "aModule: \"simple text\"" + System.getProperty("line.separator") +
                "bModule: [" + System.getProperty("line.separator") +
                "    \"arr1\"" + System.getProperty("line.separator") +
                "    \"arr2\"" + System.getProperty("line.separator") +
                "]" + System.getProperty("line.separator") +
                "cModule: 1" + System.getProperty("line.separator") +
                "zModule: " + System.getProperty("line.separator") +
                "    prop1: \"some text\"" + System.getProperty("line.separator") +
                "    prop2: 2" + System.getProperty("line.separator") +
                "    prop3:" + System.getProperty("line.separator") +
                "        prop3_1: true" + System.getProperty("line.separator");

        CodeBlockSorter sut = new CodeBlockSorter();
        List<String> codeLines = new ArrayList<>(Arrays.asList(coffeeScriptCode.split(System.getProperty("line.separator"))));
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

    @Test
    public void getSortedCodeCoffeeScriptWithEmptyLines() throws Exception {
        String coffeeScriptCode = "" +
                "zModule: " + System.getProperty("line.separator") +
                "    prop1: \"some text\"" + System.getProperty("line.separator") +
                "    prop2: 2" + System.getProperty("line.separator") +
                "    prop3:" + System.getProperty("line.separator") +
                "        prop3_1: true" + System.getProperty("line.separator") +
                System.getProperty("line.separator") +
                "aModule: \"simple text\"" + System.getProperty("line.separator") +
                "    " + System.getProperty("line.separator") +
                "cModule: 1" + System.getProperty("line.separator") +
                "bModule: [" + System.getProperty("line.separator") +
                "    \"arr1\"" + System.getProperty("line.separator") +
                "    \"arr2\"" + System.getProperty("line.separator") +
                "]" + System.getProperty("line.separator");

        String sortedCoffeeScriptCode = "" +
                "aModule: \"simple text\"" + System.getProperty("line.separator") +
                "    " + System.getProperty("line.separator") +
                "bModule: [" + System.getProperty("line.separator") +
                "    \"arr1\"" + System.getProperty("line.separator") +
                "    \"arr2\"" + System.getProperty("line.separator") +
                "]" + System.getProperty("line.separator") +
                "cModule: 1" + System.getProperty("line.separator") +
                "zModule: " + System.getProperty("line.separator") +
                "    prop1: \"some text\"" + System.getProperty("line.separator") +
                "    prop2: 2" + System.getProperty("line.separator") +
                "    prop3:" + System.getProperty("line.separator") +
                "        prop3_1: true" + System.getProperty("line.separator") +
                System.getProperty("line.separator");

        CodeBlockSorter sut = new CodeBlockSorter();
        List<String> codeLines = new ArrayList<>(Arrays.asList(coffeeScriptCode.split(System.getProperty("line.separator"))));
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }
    @Test
    public void getSortedCodeCoffeeScriptWithEmptyLinesAndJsDoc() throws Exception {
        String coffeeScriptCode = "" +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc zModule" + System.getProperty("line.separator") +
                " # @class zModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "zModule: " + System.getProperty("line.separator") +
                "    ###*" + System.getProperty("line.separator") +
                "     # Some simple property desc prop1" + System.getProperty("line.separator") +
                "     # @prop prop1" + System.getProperty("line.separator") +
                "    ###" + System.getProperty("line.separator") +
                "    prop1: \"some text\"" + System.getProperty("line.separator") +
                "    prop2: 2" + System.getProperty("line.separator") +
                "    prop3:" + System.getProperty("line.separator") +
                "        prop3_1: true" + System.getProperty("line.separator") +
                System.getProperty("line.separator") +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc aModule" + System.getProperty("line.separator") +
                " # @class aModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "aModule: \"simple text\"" + System.getProperty("line.separator") +
                "    " + System.getProperty("line.separator") +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc cModule" + System.getProperty("line.separator") +
                " # @class cModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "cModule: 1" + System.getProperty("line.separator") +
                System.getProperty("line.separator") +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc bModule" + System.getProperty("line.separator") +
                " # @class bModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "bModule: [" + System.getProperty("line.separator") +
                "    \"arr1\"" + System.getProperty("line.separator") +
                "    \"arr2\"" + System.getProperty("line.separator") +
                "]" + System.getProperty("line.separator");

        String sortedCoffeeScriptCode = "" +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc aModule" + System.getProperty("line.separator") +
                " # @class aModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "aModule: \"simple text\"" + System.getProperty("line.separator") +
                "    " + System.getProperty("line.separator") +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc bModule" + System.getProperty("line.separator") +
                " # @class bModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "bModule: [" + System.getProperty("line.separator") +
                "    \"arr1\"" + System.getProperty("line.separator") +
                "    \"arr2\"" + System.getProperty("line.separator") +
                "]" + System.getProperty("line.separator") +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc cModule" + System.getProperty("line.separator") +
                " # @class cModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "cModule: 1" + System.getProperty("line.separator") +
                System.getProperty("line.separator") +
                "###*" + System.getProperty("line.separator") +
                " # Some simple class desc zModule" + System.getProperty("line.separator") +
                " # @class zModule" + System.getProperty("line.separator") +
                "###" + System.getProperty("line.separator") +
                "zModule: " + System.getProperty("line.separator") +
                "    ###*" + System.getProperty("line.separator") +
                "     # Some simple property desc prop1" + System.getProperty("line.separator") +
                "     # @prop prop1" + System.getProperty("line.separator") +
                "    ###" + System.getProperty("line.separator") +
                "    prop1: \"some text\"" + System.getProperty("line.separator") +
                "    prop2: 2" + System.getProperty("line.separator") +
                "    prop3:" + System.getProperty("line.separator") +
                "        prop3_1: true" + System.getProperty("line.separator") +
                System.getProperty("line.separator");

        CodeBlockSorter sut = new CodeBlockSorter();
        List<String> codeLines = new ArrayList<>(Arrays.asList(coffeeScriptCode.split(System.getProperty("line.separator"))));
        String sortedText = sut.getSortedCode(codeLines);
        assertEquals("Code should be sorted by first level modules", sortedCoffeeScriptCode, sortedText);
    }

}

