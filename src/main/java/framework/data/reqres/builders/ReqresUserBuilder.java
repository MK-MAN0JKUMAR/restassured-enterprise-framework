package framework.data.reqres.builders;

import framework.data.DataContext;
import framework.data.DataSeedManager;
import framework.domain.reqres.CreateUserRequest;

public class ReqresUserBuilder {

    private String name;
    private String job;

    public ReqresUserBuilder withDeterministicName() {
        int rand = DataSeedManager.random().nextInt(10000);
        this.name = "user_" + rand + "_" +
                DataContext.get().uniqueSuffix();
        return this;
    }

    public ReqresUserBuilder withInvalidName() {
        this.name = "";
        return this;
    }

    public ReqresUserBuilder withJob(String job) {
        this.job = job;
        return this;
    }

    public ReqresUserBuilder withNullJob() {
        this.job = null;
        return this;
    }

    public CreateUserRequest build() {
        return CreateUserRequest.builder()
                .name(name)
                .job(job)
                .build();
    }
}