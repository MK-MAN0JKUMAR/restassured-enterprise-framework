package tests.reqres.stubs;

import framework.core.mock.WireMockManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class ErrorStubs {

    private ErrorStubs() {}

    public static void stubCreateUserBadRequest() {

        WireMockManager.getServer().stubFor(
                post(urlEqualTo("/users"))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("""
                                            {
                                              "error": "Bad Request",
                                              "message": "Invalid payload",
                                              "status": 400,
                                              "timestamp": "2026-01-01T00:00:00Z"
                                            }
                                            """)
                        )
        );
    }
}
