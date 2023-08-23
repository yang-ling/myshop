package ling.yang.myshop.error;

import ling.yang.myshop.exceptions.MyShopException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    @ExceptionHandler({ MyShopException.class })
    public ResponseEntity<Object> handleMyShopException(final MyShopException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.toJson(), new HttpHeaders(), ex.getHttpStatus(), request);
    }
}
