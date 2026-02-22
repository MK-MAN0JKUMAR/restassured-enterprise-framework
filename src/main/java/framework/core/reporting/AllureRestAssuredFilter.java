package framework.core.reporting;

import io.qameta.allure.restassured.AllureRestAssured;

public final class AllureRestAssuredFilter {

    private static final AllureRestAssured FILTER = new AllureRestAssured()
            .setRequestTemplate("http-request.ftl")
            .setResponseTemplate("http-response.ftl");

    private AllureRestAssuredFilter() {}

    public static AllureRestAssured get() {
        return FILTER;
    }
}