package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrequencyRuleTest {

    private FrequencyRule frequencyRule;
    private LocationsRule locationsRule;
    private SmallTransactionsRule smallTransactionsRule;

    @BeforeEach
    void setUp() {
        frequencyRule = new FrequencyRule(3, Duration.ofHours(1), 0.5);
        locationsRule = new LocationsRule(3, 0.7);
        smallTransactionsRule = new SmallTransactionsRule(3, 50.0, 0.6);
    }

    @Test
    void testApplicableReturnsTrueWhenThresholdExceeded() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 100.0, LocalDateTime.of(2024, 6, 1, 10, 0), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 50.0, LocalDateTime.of(2024, 6, 1, 10, 30), "Sofia", Channel.ATM),
                new Transaction("t3", "user1", 200.0, LocalDateTime.of(2024, 6, 1, 10, 45), "Sofia", Channel.ATM)
        );

        assertTrue(frequencyRule.applicable(transactions), "Expected rule to be applicable when threshold is exceeded");
    }

    @Test
    void testApplicableReturnsFalseWhenThresholdNotExceeded() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 100.0, LocalDateTime.of(2024, 6, 1, 10, 0), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 50.0, LocalDateTime.of(2024, 6, 1, 11, 30), "Sofia", Channel.ATM),
                new Transaction("t3", "user1", 200.0, LocalDateTime.of(2024, 6, 1, 12, 0), "Sofia", Channel.ATM)
        );

        assertFalse(frequencyRule.applicable(transactions), "Expected rule to not be applicable when transactions are outside the time window");
    }

    @Test
    void testApplicableWithEmptyTransactionsList() {
        List<Transaction> transactions = List.of();
        assertFalse(frequencyRule.applicable(transactions), "Expected rule to not be applicable for empty transactions list");
    }

    @Test
    void testApplicableWhenExactlyAtThreshold() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 100.0, LocalDateTime.of(2024, 6, 1, 10, 0), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 50.0, LocalDateTime.of(2024, 6, 1, 10, 30), "Sofia", Channel.ATM),
                new Transaction("t3", "user1", 200.0, LocalDateTime.of(2024, 6, 1, 11, 0), "Sofia", Channel.ATM)
        );

        assertTrue(frequencyRule.applicable(transactions), "Expected rule to be applicable when transaction count equals threshold within time window");
    }

    @Test
    void testLocationsRuleApplicableWhenThresholdMet() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 100.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 50.0, LocalDateTime.now(), "Plovdiv", Channel.ATM),
                new Transaction("t3", "user1", 200.0, LocalDateTime.now(), "Varna", Channel.ATM)
        );

        assertTrue(locationsRule.applicable(transactions), "Expected rule to be applicable when distinct locations meet the threshold");
    }

    @Test
    void testLocationsRuleNotApplicableWhenThresholdNotMet() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 100.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 50.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t3", "user1", 200.0, LocalDateTime.now(), "Sofia", Channel.ATM)
        );

        assertFalse(locationsRule.applicable(transactions), "Expected rule to not be applicable when distinct locations are below the threshold");
    }

    @Test
    void testLocationsRuleWithEmptyTransactionsList() {
        List<Transaction> transactions = List.of();
        assertFalse(locationsRule.applicable(transactions), "Expected rule to not be applicable for empty transactions list");
    }

    @Test
    void testSmallTransactionsRuleApplicableWhenThresholdMet() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 30.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 40.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t3", "user1", 45.0, LocalDateTime.now(), "Sofia", Channel.ATM)
        );

        assertTrue(smallTransactionsRule.applicable(transactions), "Expected rule to be applicable when small transactions meet the threshold");
    }

    @Test
    void testSmallTransactionsRuleNotApplicableWhenThresholdNotMet() {
        List<Transaction> transactions = List.of(
                new Transaction("t1", "user1", 30.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t2", "user1", 60.0, LocalDateTime.now(), "Sofia", Channel.ATM),
                new Transaction("t3", "user1", 70.0, LocalDateTime.now(), "Sofia", Channel.ATM)
        );

        assertFalse(smallTransactionsRule.applicable(transactions), "Expected rule to not be applicable when small transactions are below the threshold");
    }

    @Test
    void testSmallTransactionsRuleWithEmptyTransactionsList() {
        List<Transaction> transactions = List.of();
        assertFalse(smallTransactionsRule.applicable(transactions), "Expected rule to not be applicable for empty transactions list");
    }
}
