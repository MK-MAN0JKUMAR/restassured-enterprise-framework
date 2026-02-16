package tests.reqres.stubs;

import framework.core.mock.WireMockManager;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public final class GetUsersStub {

    private GetUsersStub() {}

    public static void stubUsersPage2() {

        WireMockManager.getServer().stubFor(
                get(urlPathEqualTo("/users"))
                        .withQueryParam("page", equalTo("2"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody("""
                                                {
                                                   "page": 2,
                                                   "per_page": 6,
                                                   "total": 12,
                                                   "total_pages": 2,
                                                   "data": [
                                                     {
                                                       "id": 1,
                                                       "email": "test1@mail.com",
                                                       "first_name": "Test",
                                                       "last_name": "User1",
                                                       "avatar": "https://reqres.in/img/faces/1-image.jpg"
                                                     },
                                                     {
                                                       "id": 2,
                                                       "email": "test2@mail.com",
                                                       "first_name": "Test",
                                                       "last_name": "User2",
                                                       "avatar": "https://reqres.in/img/faces/2-image.jpg"
                                                     }
                                                   ]
                                                 }
                                            """)
                        )
        );
    }
}
