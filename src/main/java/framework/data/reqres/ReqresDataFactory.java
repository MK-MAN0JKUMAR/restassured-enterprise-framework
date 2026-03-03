package framework.data.reqres;

import framework.data.DataSeedManager;
import framework.domain.reqres.CreateUserRequest;

import java.util.UUID;

public final class ReqresDataFactory {

    private ReqresDataFactory() {}

    public static CreateUserRequest createRandomUser() {
        return CreateUserRequest.builder()
                .name("user-" + DataSeedManager.random().nextInt(10000))
                .job("leader")
                .build();
    }
}
