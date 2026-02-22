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

        if (requestSpec.getHeaders().hasHeaderWithName("Authorization")) {
            requestSpec.replaceHeader("Authorization", "Bearer ****");
        }

        return ctx.next(requestSpec, responseSpec);
    }
}