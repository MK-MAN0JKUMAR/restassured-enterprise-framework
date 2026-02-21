package framework.domain.common;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String error;
    private String message;
    private int status;
    private String timestamp;
}
