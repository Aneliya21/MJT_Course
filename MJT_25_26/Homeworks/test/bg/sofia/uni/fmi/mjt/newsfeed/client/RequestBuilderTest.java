package Homeworks.test.bg.sofia.uni.fmi.mjt.newsfeed.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestBuilderTest {

    private RequestBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new RequestBuilder();
    }

    @Test
    void testWithPageSetsCorrectValue() {
        builder.withPage(5);
        assertEquals(5, builder.getPage(), "Page should be correctly set to 5");
    }

    @Test
    void testWithPageZeroThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withPage(0),
            "Page cannot be zero");
    }

    @Test
    void testWithPageNegativeThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withPage(-1),
            "Page cannot be less than zero");
    }

    @Test
    void testWithPageSizeSetsCorrectValue() {
        builder.withPageSize(20);
        assertEquals(20, builder.getPageSize(), "Page size should be correctly set to 20");
    }

    @Test
    void testWithPageSizeZeroThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withPageSize(0),
            "Page size cannot be zero");
    }

    @Test
    void testWithKeywordsSetsCorrectValue() {
        builder.withKeywords("science");
        assertEquals("science", builder.getKeywords(), "Keywords should be correctly set");
    }

    @Test
    void testWithKeywordsNullThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withKeywords(null),
            "Keywords cannot be null");
    }

    @Test
    void testWithKeywordsEmptyThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withKeywords(""),
            "Keywords cannot be empty");
    }

    @Test
    void testWithCategorySetsCorrectValue() {
        builder.withCategory("business");
        assertEquals("business", builder.getCategory(), "Category should be correctly set");
    }

    @Test
    void testWithCategoryNullThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withCategory(null),
            "Category cannot be null");
    }

    @Test
    void testWithCountySetsCorrectValue() {
        builder.withCounty("bg");
        assertEquals("bg", builder.getCountry(), "Country should be correctly set");
    }

    @Test
    void testWithCountyEmptyThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> builder.withCounty(""),
            "Country cannot be empty");
    }

    @Test
    void testMethodChainingWorksCorrectly() {
        RequestBuilder result = builder.withKeywords("health").withPage(1).withCategory("sports");

        assertEquals(builder, result, "The builder should return itself for chaining");
        assertEquals("health", result.getKeywords());
        assertEquals(1, result.getPage());
        assertEquals("sports", result.getCategory());
    }

    @Test
    void testValidatePresenceDoesNotThrowForValidString() {
        RequestBuilder.validatePresence("valid", "Field");
    }
}