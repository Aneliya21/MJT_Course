package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule implements Rule {

    private final int threshold;
    private final double amount;
    private final double weight;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        this.threshold = countThreshold;
        this.amount = amountThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        return transactions.stream().filter((a) -> a.transactionAmount() < amount).toList().size() >= threshold;
    }

    @Override
    public double weight() {
        return weight;
    }
}
