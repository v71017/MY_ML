package edu.rtu.stl.domain;

public class Document extends DomainObject {

    public final Sentiment sentiment;
    public final String text;

    public Document(Sentiment sentiment, String text) {
        this.sentiment = sentiment;
        this.text = text;
    }
}
