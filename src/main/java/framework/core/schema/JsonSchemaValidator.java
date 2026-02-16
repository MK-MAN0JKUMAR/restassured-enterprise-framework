package framework.core.schema;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public final class JsonSchemaValidator {

    private static final Logger log = LogManager.getLogger(JsonSchemaValidator.class);

    private JsonSchemaValidator() {}

    public static void validate(Response response, String schemaPath) {

        log.info("Validating response against schema: {}", schemaPath);

        try {
            response.then().assertThat()
                    .body(matchesJsonSchema(SchemaLoader.load(schemaPath)));

            log.info("Schema validation PASSED → {}", schemaPath);

        } catch (AssertionError e) {
            log.error("Schema validation FAILED → {}", schemaPath);
            log.error("Response body:\n{}", response.getBody().asPrettyString());
            throw e;
        }
    }
}
