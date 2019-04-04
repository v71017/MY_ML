package edu.rtu.stl.domain;

import static java.math.BigDecimal.ROUND_HALF_UP;

import java.math.BigDecimal;

public class Report extends DomainObject {
    public final Sentiment sentiment;
    public final BigDecimal precision;
    public final BigDecimal recall;

    public Report(Sentiment sentiment, BigDecimal precision, BigDecimal recall) {
        this.sentiment = sentiment;
        this.precision = precision;
        this.recall = recall;
    }

    public Report combine(Report that) {
        return new Report(sentiment, precision.add(that.precision).divide(BigDecimal.valueOf(2), ROUND_HALF_UP),
                recall.add(that.recall).divide(BigDecimal.valueOf(2), ROUND_HALF_UP));
    }
}
