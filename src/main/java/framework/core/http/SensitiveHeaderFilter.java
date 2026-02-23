package framework.core.http;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class SensitiveHeaderFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        // Clone header for logging purpose only
        String authHeader = requestSpec.getHeaders().getValue("Authorization");

        if (authHeader != null) {
            requestSpec.removeHeader("Authorization");
            requestSpec.header("Authorization", authHeader); // restore immediately
        }

        return ctx.next(requestSpec, responseSpec);
    }
}