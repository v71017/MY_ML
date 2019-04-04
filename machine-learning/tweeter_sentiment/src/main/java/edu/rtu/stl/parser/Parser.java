package edu.rtu.stl.parser;

import java.util.List;

import edu.rtu.stl.domain.Document;

public interface Parser {
    List<Document> parse(List<String> lines);
}
