package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

public class FrequencyRule implements Rule {

    private final double weight;
    private final int threshhold;
    private final TemporalAmount timeWindow;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        this.weight = weight;
        this.threshhold = transactionCountThreshold;
        this.timeWindow = timeWindow;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        List<List<Transaction>> groupedTransactions = new ArrayList<>();
        transactions.sort((t1, t2) -> t1.transactionDate().compareTo(t2.transactionDate()));
        List<Transaction> currentGroup = new ArrayList<>();
        currentGroup.add(transactions.get(0));
        groupedTransactions.add(currentGroup);

        for (int i = 1; i < transactions.size(); i++) {
            Transaction current = transactions.get(i);
            Transaction lastInGroup = currentGroup.get(currentGroup.size() - 1);
            LocalDateTime adjustedTime = lastInGroup.transactionDate().plus(timeWindow);

            if (!current.transactionDate().isAfter(adjustedTime)) {
                currentGroup.add(current);

                if (currentGroup.size() >= threshhold) {
                    return true;
                }

            } else {
                currentGroup = new ArrayList<>();
                currentGroup.add(current);
                groupedTransactions.add(currentGroup);
            }
        }
        return false;
    }

    @Override
    public double weight() {
        return weight;
    }
}
