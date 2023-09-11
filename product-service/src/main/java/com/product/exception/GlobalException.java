package com.product.exception;

import com.product.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ProductNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleProductNotFound(HttpServletRequest httpRequest, Exception exception) {
        return new ErrorResponse(httpRequest.getRequestURI(), exception.getLocalizedMessage());
    }
}
