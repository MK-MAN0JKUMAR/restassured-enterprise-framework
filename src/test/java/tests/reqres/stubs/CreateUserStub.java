package tests.reqres.stubs;

import framework.core.mock.WireMockManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class CreateUserStub {

    private CreateUserStub() {}

    public static void stubCreateUser() {

        WireMockManager.getServer().stubFor(
                post(urlEqualTo("/users"))
                        .withRequestBody(matchingJsonPath("$.name"))
                        .withRequestBody(matchingJsonPath("$.job"))
                        .atPriority(1)
                        .willReturn(
                                aResponse()
                                        .withStatus(201)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("""
                                            {
                                              "name": "user-test",
                                              "job": "leader",
                                              "id": "123",
                                              "createdAt": "2026-01-01T00:00:00.000Z"
                                            }
                                            """)
                        )
        );
    }
}
