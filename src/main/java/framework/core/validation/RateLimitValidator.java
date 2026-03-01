package framework.core.validation;

import framework.core.exception.FrameworkException;
import io.restassured.response.Response;

public final class RateLimitValidator {

    private RateLimitValidator() {}

    public static void validate(Response response) {

        validateNumericHeader(response, "X-RateLimit-Limit");
        validateNumericHeader(response, "X-RateLimit-Remaining");
        validateNumericHeader(response, "X-RateLimit-Reset");
    }

    private static void validateNumericHeader(Response response, String headerName) {

        String value = response.getHeader(headerName);

        if (value == null) {
            throw new FrameworkException("Missing header: " + headerName);
        }

        if (!value.matches("\\d+")) {
            throw new FrameworkException(
                    "Invalid numeric value for header " + headerName + ": " + value
            );
        }
    }
}