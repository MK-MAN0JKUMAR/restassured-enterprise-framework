package tests.reqres.stubs;

import framework.core.mock.WireMockManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

public final class RetrySimulationStub {

    private RetrySimulationStub() {}

    public static void stub500Then200() {

        // ---> Align admin client with dynamic WireMock port
        configureFor(
                "localhost",
                WireMockManager.getServer().port()
        );

        // First call → 500
        stubFor(get(urlEqualTo("/retry-test"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs(STARTED)
                .willReturn(
                        aResponse()
                                .withStatus(500)
                )
                .willSetStateTo("Second Attempt")
        );

        // Second call → 200
        stubFor(get(urlEqualTo("/retry-test"))
                .inScenario("Retry Scenario")
                .whenScenarioStateIs("Second Attempt")
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withBody("{\"success\":true}")
                )
        );
    }
}