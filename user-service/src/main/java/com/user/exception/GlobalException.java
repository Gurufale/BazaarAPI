package com.user.exception;

import com.user.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleUserAlreadyExists(HttpServletRequest httpRequest, Exception exception) {
        return new ErrorResponse(httpRequest.getRequestURI(), exception.getLocalizedMessage());
    }
}
