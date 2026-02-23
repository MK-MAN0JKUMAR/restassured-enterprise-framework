package framework.domain.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRepoRequest {

    private String name;
    private String description;

    @JsonProperty("private")
    private boolean isPrivate;
}