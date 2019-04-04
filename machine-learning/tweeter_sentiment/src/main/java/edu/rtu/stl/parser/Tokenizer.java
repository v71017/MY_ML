package edu.rtu.stl.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class Tokenizer {

    public final Set<String> stopWords;

    public Tokenizer(Set<String> stopWords) {
        this.stopWords = stopWords;
    }

    public Collection<String> tokenize(String line) {
        return Arrays.stream(line.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+")).map(String::trim)
                .filter(x -> !x.isEmpty() && !x.startsWith("@") && !stopWords.contains(x)).collect(Collectors.toList());
    }
}
