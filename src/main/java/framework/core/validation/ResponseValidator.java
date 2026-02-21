package framework.core.validation;

import framework.core.http.ResponseSpecFactory;
import framework.core.schema.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.hamcrest.Matchers.lessThan;

public final class ResponseValidator {

    private static final Logger log =
            LogManager.getLogger(ResponseValidator.class);

    // Default SLA (can later move to FrameworkConfig)
    private static final long DEFAULT_SLA_MS = 3000;

    private ResponseValidator() {}

    // ========================= SUCCESS =========================

    public static void success(Response response) {
        log.info("Validating SUCCESS response (2xx JSON expected)");

        int status = response.getStatusCode();

        if (status < 200 || status >= 300) {
            throw new AssertionError(
                    "Expected 2xx success but was " + status);
        }

        response.then()
                .contentType("application/json")
                .time(lessThan(DEFAULT_SLA_MS));
    }

    public static void successNoContent(Response response) {
        log.info("Validating SUCCESS response (204 No Content)");
        response.then()
                .spec(ResponseSpecFactory.successNoContent())
                .time(lessThan(DEFAULT_SLA_MS));
    }

    // ========================= STATUS ONLY =========================

    public static void status(Response response, int expectedStatus) {
        log.info("Validating status code → expected: {}", expectedStatus);
        response.then()
                .spec(ResponseSpecFactory.statusWithoutJson(expectedStatus))
                .time(lessThan(DEFAULT_SLA_MS));
    }

    public static void statusWithoutJson(Response response, int expectedStatus) {
        log.info("Validating status (no JSON enforced) → expected: {}", expectedStatus);
        response.then()
                .spec(ResponseSpecFactory.statusWithoutJson(expectedStatus))
                .time(lessThan(DEFAULT_SLA_MS));
    }

    // ========================= CLIENT ERROR =========================

    public static void clientError(Response response, int expectedStatus) {
        log.info("Validating CLIENT ERROR → expected: {}", expectedStatus);
        response.then()
                .spec(ResponseSpecFactory.clientError(expectedStatus))
                .time(lessThan(DEFAULT_SLA_MS));
    }

    // ========================= SCHEMA =========================

    public static void schema(Response response, String schemaPath) {
        log.info("Validating response schema → {}", schemaPath);
        JsonSchemaValidator.validate(response, schemaPath);
    }

    // ========================= COMBINED HELPERS =========================

    public static void successWithSchema(Response response, String schemaPath) {
        success(response);
        schema(response, schemaPath);
    }

    public static void successWithSchemaAndSla(Response response,
                                               String schemaPath,
                                               long maxResponseTimeMs) {

        log.info("Validating SUCCESS + Schema + SLA ({} ms)", maxResponseTimeMs);

        response.then()
                .spec(ResponseSpecFactory.successJson())
                .time(lessThan(maxResponseTimeMs));

        JsonSchemaValidator.validate(response, schemaPath);
    }

    public static void clientErrorWithSchema(Response response,
                                             int expectedStatus,
                                             String schemaPath) {

        clientError(response, expectedStatus);
        schema(response, schemaPath);
    }
}