package framework.core.schema;

import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

public final class JsonSchemaValidator {

    private JsonSchemaValidator() {}

    public static void validate(Response response, String schemaPath) {
        response.then().assertThat()
                .body(matchesJsonSchema(SchemaLoader.load(schemaPath)));
    }
}
