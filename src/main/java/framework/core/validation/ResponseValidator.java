package framework.core.validation;

import framework.core.http.ResponseSpecFactory;
import framework.core.schema.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ResponseValidator {

    private static final Logger log = LogManager.getLogger(ResponseValidator.class);

    private ResponseValidator() {}

    // ================= SUCCESS =================
    public static void success(Response response) {
        log.info("Validating SUCCESS response");
        response.then().spec(ResponseSpecFactory.success(true));
    }

    public static void successNoContent(Response response) {
        log.info("Validating SUCCESS response (no content expected)");
        response.then().spec(ResponseSpecFactory.success(false));
    }

    // ================= STATUS =================
    public static void status(Response response, int expectedStatus) {
        log.info("Validating status code → expected: {}", expectedStatus);
        response.then().spec(ResponseSpecFactory.status(expectedStatus, true));
    }

    public static void statusNoJson(Response response, int expectedStatus) {
        log.info("Validating status code (no JSON enforced) → expected: {}", expectedStatus);
        response.then().spec(ResponseSpecFactory.status(expectedStatus, false));
    }

    // ================= CLIENT ERROR =================
    public static void clientError(Response response, int expectedStatus) {
        log.info("Validating CLIENT ERROR → expected: {}", expectedStatus);
        response.then().spec(ResponseSpecFactory.clientError(expectedStatus));
    }

    // ================= SCHEMA =================
    public static void schema(Response response, String schemaPath) {
        log.info("Validating response schema → {}", schemaPath);
        JsonSchemaValidator.validate(response, schemaPath);
    }

    // ================= COMBINED HELPERS =================
    public static void successWithSchema(Response response, String schemaPath) {
        success(response);
        schema(response, schemaPath);
    }

    public static void clientErrorWithSchema(Response response, int status, String schemaPath) {
        clientError(response, status);
        schema(response, schemaPath);
    }
}
