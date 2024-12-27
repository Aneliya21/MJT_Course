package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private List<Transaction> transactions;
    private List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        if (reader == null || rules == null) {
            throw new IllegalArgumentException();
        }
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            bufferedReader.readLine();

            this.transactions = bufferedReader.lines().map(Transaction::of).toList();
            double weightsSum = rules.stream().mapToDouble(Rule::weight).sum();

            if (Double.compare(weightsSum, 1.0) != 0) {
                throw new IllegalArgumentException("Weight sum cannot be greater than 1.0");
            }
            this.rules = rules;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream().map(Transaction::accountID).distinct().toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        Map<Channel, Long> longMap =
                transactions.stream().collect(Collectors.groupingBy(Transaction::channel, Collectors.counting()));

        Map<Channel, Integer> intMap = longMap.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().intValue()));
        return intMap;
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        return transactions.stream().filter(transaction ->
                transaction.accountID().equals(accountID)).mapToDouble(Transaction::transactionAmount).sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        return transactions.stream().filter(transaction -> transaction.accountID().equals(accountId)).toList();
    }

    @Override
    public double accountRating(String accountId) {
        return 0;
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        Map<String, List<Transaction>> groupedTransactions = transactions.stream().collect(
                Collectors.groupingBy(Transaction::accountID));
        Map<String, Double> r = groupedTransactions.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, entry -> rules.stream().filter(
                        rule -> rule.applicable(entry.getValue())).mapToDouble(Rule::weight).sum()));

        List<Map.Entry<String, Double>> entryList = new ArrayList<>(r.entrySet());

        entryList.sort(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()));

        SortedMap<String, Double> sortedMap = new TreeMap<>(
                (entry1, entry2) -> Double.compare(r.get(entry2), r.get(entry1))
        );
        for (Map.Entry<String, Double> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
