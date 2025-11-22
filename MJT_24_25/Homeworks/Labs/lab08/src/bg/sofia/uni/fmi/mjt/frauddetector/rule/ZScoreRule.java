package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule {

    private final double weight;
    private final double threshhold;

    public ZScoreRule(double zScoreThreshold, double weight) {
        this.weight = weight;
        this.threshhold = zScoreThreshold;
    }

    public double getAverage(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::transactionAmount)
                .average()
                .orElse(0);
    }

    private double getStandardDeviation(List<Transaction> transactions, double mean) {
        double variance = transactions.stream()
                .mapToDouble(transaction -> Math.pow(transaction.transactionAmount() - mean, 2))
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double mean = getAverage(transactions);
        double stdDev = getStandardDeviation(transactions, mean);

        if (stdDev == 0) {
            return false;
        }
        return transactions.stream().mapToDouble(a -> (a.transactionAmount() - mean) / stdDev)
                .anyMatch(a -> Double.compare(a, threshhold) > 0);
    }

    @Override
    public double weight() {
        return weight;
    }
}
