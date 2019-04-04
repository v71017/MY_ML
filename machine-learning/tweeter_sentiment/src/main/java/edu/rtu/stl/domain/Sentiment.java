package edu.rtu.stl.domain;

public enum Sentiment {
	positive("positive"), neutral("neutral"), negative("negative");

    public final String value;

    Sentiment(String value) {
        this.value = value;
    }

    public static Sentiment fromValue(String value) {
        for (Sentiment sentiment : Sentiment.values()) {
            if (sentiment.value.equals(value)) {
                return sentiment;
            }
        }
        throw new IllegalArgumentException("Not a valid Sentiment value: " + value);
    }
}
