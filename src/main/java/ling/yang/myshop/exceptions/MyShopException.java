package ling.yang.myshop.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MyShopException extends RuntimeException {
    private final String errorMessage;
    private final HttpStatus httpStatus;

    public MyShopException(MyShopExceptionAttributes attributes) {
        super(attributes.getErrMsg());
        this.errorMessage = attributes.getErrMsg();
        this.httpStatus = attributes.getHttpStatus();
    }

    public Map<String, String> toJson() {
        return new HashMap<>() {{
            put("httpStatusCode", String.valueOf(getHttpStatus().value()));
            put("httpStatus", getHttpStatus().getReasonPhrase());
            put("message", getErrorMessage());
        }};
    }
}
