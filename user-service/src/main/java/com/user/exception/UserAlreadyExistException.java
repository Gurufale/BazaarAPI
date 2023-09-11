package com.user.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String userAlreadyExists) {
        super(userAlreadyExists);
    }
}
