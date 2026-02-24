package Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.handler;

import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.BadRequestException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.NewsApiException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.ServerErrorException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.TooManyRequestsException;
import Homeworks.src.bg.sofia.uni.fmi.mjt.newsfeed.exception.UnauthorizedException;

/**
 * Utility class responsible for interpreting HTTP response status code
 * and mapping them to domain-specific exceptions.
 * <p>
 *     This handler centralizes error logic to ensure consistent error reporting
 *     across the News Feed library, specifically handling cases documented
 *     by the News API.
 * </p>
 */
public class NewApiErrorHandler {

    private static final int OK_START = 200;
    private static final int OK_END = 201;
    private static final int BAD_REQUEST = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int TOO_MANY_REQUESTS = 429;
    private static final int SERVER_ERROR_START = 500;
    private static final int SERVER_ERROR_END = 599;

    /**
     * Maps HTTP status codes to domain-specific exceptions.
     * * @param responseCode The HTTP status code returned by the API.
     * @param errorBody The body of the error response.
     * @throws NewsApiException The specific exception corresponding to the error code.
     */
    public static void handle(int responseCode, String errorBody) throws NewsApiException {
        if (errorBody == null) {
            errorBody = "No error body provided";
        }

        if (responseCode == BAD_REQUEST) {
            throw new BadRequestException(errorBody);
        } else if (responseCode == UNAUTHORIZED) {
            throw new UnauthorizedException(errorBody);
        } else if (responseCode == TOO_MANY_REQUESTS) {
            throw new TooManyRequestsException(errorBody);
        } else if (isServerError(responseCode)) {
            throw new ServerErrorException(errorBody);
        } else {
            throw new NewsApiException("Unknown error (status " + responseCode + "): " + errorBody);
        }
    }

    /**
     * Determines if the provided HTTP status code indicates a successful request.
     * @param code The HTTP status code to check.
     * @return {@code true} if the code is withing the 200-201 range; {@code false} otherwise.
     */
    public static boolean isSuccess(int code) {
        return code >= OK_START && code <= OK_END;
    }

    /**
     * Checks if the provided HTTP status code indicates a server-side error.
     * @param code The HTTP status code to check.
     * @return {@code true} if the code is in the 500-599 range.
     */
    private static boolean isServerError(int code) {
        return code >= SERVER_ERROR_START && code <= SERVER_ERROR_END;
    }
}
