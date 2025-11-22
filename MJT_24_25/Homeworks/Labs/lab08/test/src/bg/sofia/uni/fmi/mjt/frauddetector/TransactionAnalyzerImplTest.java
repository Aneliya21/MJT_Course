package bg.sofia.uni.fmi.mjt.frauddetector;

import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerImpl;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionAnalyzerImplTest {

    private static final String SAMPLE_TRANSACTIONS = "timestamp,accountID,channel,transactionAmount\n" +
            "2024-06-01T10:00:00,user1,ATM,100.00\n" +
            "2024-06-01T11:00:00,user2,POS,200.00\n" +
            "2024-06-01T12:00:00,user1,WEB,50.00\n" +
            "2024-06-01T13:00:00,user3,ATM,300.00";

    private TransactionAnalyzerImpl analyzer;
    private List<Rule> mockRules;

    @BeforeEach
    void setUp() {
        // Mock rules
        Rule rule1 = mock(Rule.class);
        when(rule1.weight()).thenReturn(0.5);
        when(rule1.applicable(any())).thenReturn(true);

        Rule rule2 = mock(Rule.class);
        when(rule2.weight()).thenReturn(0.5);
        when(rule2.applicable(any())).thenReturn(false);

        mockRules = List.of(rule1, rule2);

        // Initialize TransactionAnalyzerImpl
        Reader reader = new StringReader(SAMPLE_TRANSACTIONS);
        analyzer = new TransactionAnalyzerImpl(reader, mockRules);
    }

    @Test
    void testAllTransactions() {
        List<Transaction> transactions = analyzer.allTransactions();
        assertEquals(4, transactions.size(), "Expected 4 transactions in total");
    }

    @Test
    void testAllAccountIDs() {
        List<String> accountIDs = analyzer.allAccountIDs();
        assertEquals(3, accountIDs.size(), "Expected 3 distinct account IDs");
        assertTrue(accountIDs.contains("user1"));
        assertTrue(accountIDs.contains("user2"));
        assertTrue(accountIDs.contains("user3"));
    }

    @Test
    void testTransactionCountByChannel() {
        Map<Channel, Integer> counts = analyzer.transactionCountByChannel();
        assertEquals(2, counts.get(Channel.ATM), "Expected 2 ATM transactions");
        assertEquals(1, counts.get(Channel.ONLINE), "Expected 1 POS transaction");
        assertEquals(1, counts.get(Channel.BRANCH), "Expected 1 WEB transaction");
    }

    @Test
    void testAmountSpentByUser() {
        double amountUser1 = analyzer.amountSpentByUser("user1");
        assertEquals(150.0, amountUser1, 0.001, "User1 total spent amount incorrect");
    }

    @Test
    void testAllTransactionsByUser() {
        List<Transaction> user1Transactions = analyzer.allTransactionsByUser("user1");
        assertEquals(2, user1Transactions.size(), "User1 should have 2 transactions");
    }

    @Test
    void testAccountsRisk() {
        SortedMap<String, Double> riskMap = analyzer.accountsRisk();
        assertNotNull(riskMap, "Risk map should not be null");
        assertEquals(3, riskMap.size(), "Expected risk ratings for 3 accounts");

        assertEquals(0.5, riskMap.get("user1"), 0.001, "User1 risk rating incorrect");
    }

    @Test
    void testAmountSpentByUserWithInvalidAccountID() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(null),
                "Expected IllegalArgumentException for null account ID");
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(""),
                "Expected IllegalArgumentException for empty account ID");
    }
}
