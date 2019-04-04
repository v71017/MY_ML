package edu.rtu.stl.parser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UniqueTokenizer extends Tokenizer {
    public UniqueTokenizer(Set<String> stopWords) {
        super(stopWords);
    }

    @Override
    public Collection<String> tokenize(String line) {
        return new HashSet<>(super.tokenize(line));
    }
}
