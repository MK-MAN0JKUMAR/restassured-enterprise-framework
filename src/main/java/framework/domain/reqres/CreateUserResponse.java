package framework.domain.reqres;

import lombok.Getter;

@Getter
public class CreateUserResponse {

    private String name;
    private String job;
    private String id;
    private String createdAt;
}
