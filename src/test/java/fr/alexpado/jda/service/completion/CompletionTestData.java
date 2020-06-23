package fr.alexpado.jda.service.completion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CompletionTestData {

    static HashMap<Integer, List<String>> simpleInput() {

        HashMap<Integer, List<String>> input = new HashMap<>();
        input.put(1, Arrays.asList("language", "switch", "java"));
        input.put(2, Arrays.asList("language", "switch", "php"));
        input.put(3, Arrays.asList("language", "switch", "python"));
        input.put(4, Arrays.asList("language", "switch", "javascript"));
        input.put(5, Arrays.asList("language", "switch", "kotlin"));
        input.put(6, Arrays.asList("language", "switch", "c#"));
        input.put(7, Arrays.asList("language", "show", "message"));
        return input;
    }

    static HashMap<Integer, List<String>> dynamicInput() {

        HashMap<Integer, List<String>> input = new HashMap<>();
        input.put(1, Arrays.asList("language", "switch", "{lang}"));
        input.put(2, Arrays.asList("language", "show", "message"));
        return input;
    }

    static HashMap<String, List<String>> dynamicOptions() {

        HashMap<String, List<String>> options = new HashMap<>();
        options.put("lang", Arrays.asList("java", "php", "python", "javascript", "kotlin", "c#"));
        return options;
    }

    static HashMap<Integer, List<String>> passThroughInput() {

        HashMap<Integer, List<String>> input = new HashMap<>();
        input.put(1, Arrays.asList("language", "switch", "[lang]"));
        input.put(2, Arrays.asList("language", "show", "message"));
        return input;
    }

    static void assertListEquals(List<String> expected, List<String> actual) {

        expected.sort(String::compareTo);
        actual.sort(String::compareTo);
        assertArrayEquals(expected.toArray(new String[0]), actual.toArray(new String[0]));
    }

}
