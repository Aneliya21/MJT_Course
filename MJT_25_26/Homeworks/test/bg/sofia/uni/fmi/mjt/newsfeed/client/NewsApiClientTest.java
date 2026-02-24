package Homeworks.test.bg.sofia.uni.fmi.mjt.newsfeed.client;

import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.cache.NewsCache;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.config.NewsApiConfig;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.BadRequestException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.NewsApiException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.model.Article;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.model.NewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewsApiClientTest {

    private NewsApiConfig configMock;
    private HttpClient httpClientMock;
    private NewsCache cacheMock;
    private HttpResponse<String> httpResponseMock;
    private NewsApiClient client;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        configMock = mock(NewsApiConfig.class);
        httpClientMock = mock(HttpClient.class);
        cacheMock = mock(NewsCache.class);
        httpResponseMock = mock(HttpResponse.class);

        when(configMock.baseURL()).thenReturn("http://api.news.com");
        when(configMock.apiKey()).thenReturn("test-key");
        when(configMock.readTimeoutMillis()).thenReturn(5000);

        client = new NewsApiClient(configMock, httpClientMock, cacheMock);
    }

    @Test
    void testConstructorThrowsExceptionWhenConfigIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new NewsApiClient(null, httpClientMock, cacheMock),
            "Constructor should throw if config is null");
    }

    @Test
    void testGetTopHeadlinesReturnsCachedResponseWhenAvailable() throws Exception {
        RequestBuilder builder = new RequestBuilder().withKeywords("java");
        String cachedJson = "{\"status\":\"ok\",\"totalResults\":1,\"articles\":[]}";

        when(cacheMock.get(ArgumentMatchers.anyString())).thenReturn(Optional.of(cachedJson));

        NewsResponse response = client.getTopHeadlines(builder);

        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        verify(httpClientMock, never()).send(any(), any());
    }

    @Test
    void testGetTopHeadlinesFetchesFromNetworkWhenCacheIsEmpty() throws Exception {
        RequestBuilder builder = new RequestBuilder().withKeywords("java");
        String responseJson = "{\"status\":\"ok\",\"totalResults\":0,\"articles\":[]}";

        when(cacheMock.get(any())).thenReturn(Optional.empty());
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(responseJson);
        when(httpClientMock.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(httpResponseMock);

        NewsResponse response = client.getTopHeadlines(builder);

        assertNotNull(response);
        verify(cacheMock).put(any(), ArgumentMatchers.eq(responseJson));
    }

    @Test
    void testGetTopHeadlinesThrowsNewsApiExceptionOnHttpError() throws Exception {
        RequestBuilder builder = new RequestBuilder().withKeywords("error");

        when(cacheMock.get(any())).thenReturn(Optional.empty());
        when(httpResponseMock.statusCode()).thenReturn(400); // Bad Request
        when(httpResponseMock.body()).thenReturn("Bad API Key");
        when(httpClientMock.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(httpResponseMock);

        assertThrows(BadRequestException.class, () -> client.getTopHeadlines(builder),
            "Should throw BadRequestException when API returns 400");
    }

    @Test
    void testGetTopHeadlinesThrowsNewsApiExceptionOnNetworkFailure() throws Exception {
        RequestBuilder builder = new RequestBuilder().withKeywords("fail");

        when(cacheMock.get(any())).thenReturn(Optional.empty());
        when(httpClientMock.send(any(), any())).thenThrow(new IOException("Connection lost"));

        assertThrows(NewsApiException.class, () -> client.getTopHeadlines(builder),
            "Should wrap IOException into NewsApiException");
    }

    @Test
    void testGetTopHeadlinesEncodesKeywordsCorrectly() throws Exception {
        RequestBuilder builder = new RequestBuilder().withKeywords("java 17!");

        when(cacheMock.get(any())).thenReturn(Optional.empty());
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn("{\"status\":\"ok\"}");
        when(httpClientMock.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(httpResponseMock);

        client.getTopHeadlines(builder);

        verify(httpClientMock).send(ArgumentMatchers.argThat(request ->
            request.uri().toString().contains("q=java+17%21")), any());
    }

    @Test
    void testGetTopHeadlinesThrowsExceptionWhenBuilderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> client.getTopHeadlines(null),
            "Should throw if RequestBuilder is null");
    }

    @Test
    void testGetTopHeadlinesParsesFullJsonResponseCorrectly() throws Exception {
        String fullJson = """
        {
            "status": "ok",
            "totalResults": 1,
            "articles": [
                {
                    "source": { "id": "cnn", "name": "CNN" },
                    "author": "John Doe",
                    "title": "Java News",
                    "description": "Some description",
                    "url": "http://news.com/java",
                    "publishedAt": "2024-01-01T10:00:00Z"
                }
            ]
        }
        """;

        when(cacheMock.get(any())).thenReturn(Optional.empty());
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(fullJson);
        when(httpClientMock.send(any(), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
            .thenReturn(httpResponseMock);

        NewsResponse response = client.getTopHeadlines(new RequestBuilder().withKeywords("java"));

        assertNotNull(response);
        assertEquals("ok", response.getStatus());
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getArticles().length);

        Article article = response.getArticles()[0];
        assertEquals("John Doe", article.getAuthor());
        assertEquals("Java News", article.getTitle());
        assertEquals("http://news.com/java", article.getUrl());

        assertNotNull(article.getSource());
        assertEquals("cnn", article.getSource().getId());
        assertEquals("CNN", article.getSource().getName());
    }
}