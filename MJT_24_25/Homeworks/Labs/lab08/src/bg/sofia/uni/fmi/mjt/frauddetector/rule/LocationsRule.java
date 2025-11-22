package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class LocationsRule implements Rule {

    private final double weight;
    private final int threshhold;

    public LocationsRule(int threshold, double weight) {
        this.weight = weight;
        this.threshhold = threshold;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        return transactions.stream().collect(Collectors.groupingBy(
                Transaction::location, Collectors.counting())).size() >= threshhold;
    }

    @Override
    public double weight() {
        return weight;
    }
}
