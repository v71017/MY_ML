package main.java;
import java.util.List;

/**
 * Created by dewadkar on 8/12/2016.
 */
public class TfIdf {

    public double tfCalculator(String[] totalterms, String termToCheck) {
        double count = 0;
        for (String s : totalterms) {
            if (s.equalsIgnoreCase(String.valueOf(termToCheck))) {
                count++;
            }
        }
        return count / totalterms.length;
    }

    public double idfCalculator(List<String[]> allTerms, String termToCheck) {
        double count = 0;
        for (String[] ss : allTerms) {
            for (String s : ss) {
                if (s.equalsIgnoreCase(String.valueOf(termToCheck))) {
                    count++;
                    break;
                }
            }
        }
        return 1 + Math.log(allTerms.size() / count);
    }
}