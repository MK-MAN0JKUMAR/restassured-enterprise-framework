package framework.data.reqres;

import framework.models.reqres.CreateUserRequest;

import java.util.UUID;

public final class ReqresDataFactory {

    private ReqresDataFactory() {}

    public static CreateUserRequest createRandomUser() {
        return CreateUserRequest.builder()
                .name("user-" + UUID.randomUUID().toString().substring(0, 6))
                .job("leader")
                .build();
    }
}
