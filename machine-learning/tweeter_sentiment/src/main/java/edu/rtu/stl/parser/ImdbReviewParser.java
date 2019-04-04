package edu.rtu.stl.parser;

import java.util.List;
import java.util.stream.Collectors;

import edu.rtu.stl.domain.Document;
import edu.rtu.stl.domain.Sentiment;

public class ImdbReviewParser implements Parser {

    private final Sentiment sentiment;

    public ImdbReviewParser(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public List<Document> parse(List<String> lines) {
        return lines.stream().map(x -> new Document(sentiment, x)).collect(Collectors.toList());
    }
}
