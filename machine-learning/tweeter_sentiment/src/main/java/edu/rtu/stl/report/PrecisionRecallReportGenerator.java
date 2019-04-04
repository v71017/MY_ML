package edu.rtu.stl.report;

import static java.math.BigDecimal.ROUND_HALF_UP;

import java.math.BigDecimal;
import java.util.List;

import edu.rtu.stl.classifier.Classifier.Result;
import edu.rtu.stl.domain.Report;
import edu.rtu.stl.domain.Sentiment;

public class PrecisionRecallReportGenerator {

    public Report generateFor(Sentiment sentiment, List<Result> results) {
        int[] totals = new int[Sentiment.values().length];
        int correct = 0;
        int resultSize = 0;
        for (Result result : results) {
            totals[result.document.sentiment.ordinal()]++;
            if (sentiment != result.sentiment) {
                continue;
            }
            resultSize++;
            if (result.sentiment == result.document.sentiment) {
                correct++;
            }
        }
        return new Report(sentiment, computePercentage(correct, resultSize), computePercentage(correct, totals[sentiment.ordinal()]));
    }

    private BigDecimal computePercentage(int a, int b) {
        if (a == 0 && b == 0) {
            return BigDecimal.ONE.multiply(new BigDecimal(100));
        }
        if (b == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(a).multiply(new BigDecimal(100)).divide(new BigDecimal(b), ROUND_HALF_UP);
    }
}
