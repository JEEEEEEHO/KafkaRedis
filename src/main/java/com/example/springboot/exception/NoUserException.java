package com.example.springboot.exception;

public class NoUserException extends RuntimeException{
    public NoUserException() {
        super("No User Found");
    }
}
