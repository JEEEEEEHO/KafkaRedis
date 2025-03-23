package com.example.springboot.exception;

public class NoLockException extends RuntimeException {
    public NoLockException() {
        super("Redisson Lock Failure");
    }
}
