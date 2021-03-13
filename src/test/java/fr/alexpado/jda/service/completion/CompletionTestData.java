package fr.alexpado.jda.service.completion;

import fr.alexpado.jda.services.syntax.SyntaxUtils;
import fr.alexpado.jda.services.syntax.interfaces.ISyntax;
import fr.alexpado.jda.services.syntax.interfaces.ISyntaxContainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class CompletionTestData {

    static final Map<String, List<String>> EMPTY_MAP = new HashMap<>();

    static final Map<String, List<String>> OPTIONS = new HashMap<>() {{
        this.put("lang", Arrays.asList("java", "php", "python", "javascript", "kotlin", "c#"));
    }};

    static final Map<Integer, ISyntaxContainer> EMPTY_INPUT = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "", 1));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "language", 2));
    }};

    static final Map<Integer, ISyntaxContainer> COLLISION_INPUT_A = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "collide help", 1));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "collide [message]", 2));
    }};

    static final Map<Integer, ISyntaxContainer> COLLISION_INPUT_B = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "collide help", 2));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "collide [message]", 1));
    }};

    static final Map<Integer, ISyntaxContainer> REGEX_INPUT = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "regex /[0-9]+/", 1));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "regex /[a-z]+/", 3));
        this.put(3, SyntaxUtils.toContainer(EMPTY_MAP, "regex /strict/", 2));
    }};

    static final Map<Integer, ISyntaxContainer> SIMPLE_INPUT = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "language switch java", 1));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "language switch php", 2));
        this.put(3, SyntaxUtils.toContainer(EMPTY_MAP, "language switch python", 3));
        this.put(4, SyntaxUtils.toContainer(EMPTY_MAP, "language switch javascript", 4));
        this.put(5, SyntaxUtils.toContainer(EMPTY_MAP, "language switch kotlin", 5));
        this.put(6, SyntaxUtils.toContainer(EMPTY_MAP, "language switch c#", 6));
        this.put(7, SyntaxUtils.toContainer(EMPTY_MAP, "language show message", 7));
    }};

    static final Map<Integer, ISyntaxContainer> DYNAMIC_INPUT = new HashMap<>() {{
       this.put(1, SyntaxUtils.toContainer(OPTIONS, "language switch {lang}", 1));
       this.put(2, SyntaxUtils.toContainer(OPTIONS, "language show message", 2));
    }};

    static final Map<Integer, ISyntaxContainer> PASS_THROUGH_INPUT = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "language switch [lang]", 1));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "language show message", 2));
    }};

    static final Map<Integer, ISyntaxContainer> FILLER_INPUT = new HashMap<>() {{
        this.put(1, SyntaxUtils.toContainer(EMPTY_MAP, "language switch [lang]", 1));
        this.put(2, SyntaxUtils.toContainer(EMPTY_MAP, "language message msg...", 2));
    }};

    static void assertListEquals(List<String> expected, List<String> actual) {

        expected.sort(String::compareTo);
        actual.sort(String::compareTo);
        assertArrayEquals(expected.toArray(new String[0]), actual.toArray(new String[0]));
    }

}
