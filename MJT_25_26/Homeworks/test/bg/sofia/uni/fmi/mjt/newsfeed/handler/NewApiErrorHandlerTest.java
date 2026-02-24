package Homeworks.test.bg.sofia.uni.fmi.mjt.newsfeed.handler;

import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.BadRequestException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.NewsApiException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.ServerErrorException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.TooManyRequestsException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewApiErrorHandlerTest {

    private static final String TEST_ERROR_BODY = "Error message";

    @Test
    void testHandleBadRequestThrowsBadRequestException() {
        assertThrows(BadRequestException.class, () -> NewApiErrorHandler.handle(400, TEST_ERROR_BODY),
            "Should throw BadRequestException for 400 status code");
    }

    @Test
    void testHandleUnauthorizedThrowsUnauthorizedException() {
        assertThrows(UnauthorizedException.class, () -> NewApiErrorHandler.handle(401, TEST_ERROR_BODY),
            "Should throw UnauthorizedException for 401 status code");
    }

    @Test
    void testHandleTooManyRequestsThrowsTooManyRequestsException() {
        assertThrows(TooManyRequestsException.class, () -> NewApiErrorHandler.handle(429, TEST_ERROR_BODY),
            "Should throw TooManyRequestsException for 429 status code");
    }

    @Test
    void testHandleServerErrorLowerBoundThrowsServerErrorException() {
        assertThrows(ServerErrorException.class, () -> NewApiErrorHandler.handle(500, TEST_ERROR_BODY),
            "Should throw ServerErrorException for 500 status code");
    }

    @Test
    void testHandleServerErrorUpperBoundThrowsServerErrorException() {
        assertThrows(ServerErrorException.class, () -> NewApiErrorHandler.handle(599, TEST_ERROR_BODY),
            "Should throw ServerErrorException for 599 status code");
    }

    @Test
    void testHandleUnknownCodeThrowsGeneralNewsApiException() {
        assertThrows(NewsApiException.class, () -> NewApiErrorHandler.handle(404, TEST_ERROR_BODY),
            "Should throw generic NewsApiException for unhandled 404 code");
    }

    @Test
    void testHandleNullBodyUsesDefaultMessage() {
        NewsApiException exception = assertThrows(BadRequestException.class,
            () -> NewApiErrorHandler.handle(400, null));

        assertEquals("Bad request No error body provided", exception.getMessage(),
            "Should use default message when body is null");
    }

    @Test
    void testIsSuccessReturnsTrueFor200() {
        assertTrue(NewApiErrorHandler.isSuccess(200), "Code 200 should be successful");
    }

    @Test
    void testIsSuccessReturnsTrueFor201() {
        assertTrue(NewApiErrorHandler.isSuccess(201), "Code 201 should be successful");
    }

    @Test
    void testIsSuccessReturnsFalseFor400() {
        assertFalse(NewApiErrorHandler.isSuccess(400), "Code 400 should not be successful");
    }

    @Test
    void testIsSuccessReturnsFalseForInternalServerError() {
        assertFalse(NewApiErrorHandler.isSuccess(500), "Code 500 should not be successful");
    }

    @Test
    void testIsSuccessReturnsTrueForLowerBound200() {
        assertTrue(NewApiErrorHandler.isSuccess(200),
            "Code 200 (OK) should be considered successful");
    }

    @Test
    void testIsSuccessReturnsTrueForUpperBound201() {
        assertTrue(NewApiErrorHandler.isSuccess(201),
            "Code 201 (Created) should be considered successful");
    }

    @Test
    void testIsSuccessReturnsFalseForJustBelowRange199() {
        assertFalse(NewApiErrorHandler.isSuccess(199),
            "Code 199 is not in the success range");
    }

    @Test
    void testIsSuccessReturnsFalseForJustAboveRange202() {
        assertFalse(NewApiErrorHandler.isSuccess(202),
            "Code 202 is outside the defined success range for this API");
    }

    @Test
    void testHandleServerErrorLowerBound500ThrowsException() {
        assertThrows(ServerErrorException.class, () -> NewApiErrorHandler.handle(500, TEST_ERROR_BODY),
            "Should throw ServerErrorException exactly at the lower bound (500)");
    }

    @Test
    void testHandleServerErrorUpperBound599ThrowsException() {
        assertThrows(ServerErrorException.class, () -> NewApiErrorHandler.handle(599, TEST_ERROR_BODY),
            "Should throw ServerErrorException exactly at the upper bound (599)");
    }

    @Test
    void testHandleCodeJustBelowServerErrorRange499ThrowsException() {
        assertThrows(NewsApiException.class, () -> NewApiErrorHandler.handle(499, TEST_ERROR_BODY),
            "Code 499 is below the 500-599 range and should fall through to generic NewsApiException");
    }

    @Test
    void testHandleCodeJustAboveServerErrorRange600ThrowsException() {
        assertThrows(NewsApiException.class, () -> NewApiErrorHandler.handle(600, TEST_ERROR_BODY),
            "Code 600 is above the 500-599 range and should fall through to generic NewsApiException");
    }
}