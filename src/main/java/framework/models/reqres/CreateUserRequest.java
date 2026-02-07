package framework.models.reqres;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {

    private final String name;
    private final String job;
}
