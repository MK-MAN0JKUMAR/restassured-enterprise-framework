package framework.domain.reqres;

import lombok.Getter;
import java.util.List;

@Getter
public class GetUsersResponse {
    private int page;
    private List<User> data;

    @Getter
    public static class User {
        private int id;
        private String email;
    }
}
