package com.example.springboot.exception;

public class NoLockException extends Exception {
    public NoLockException() {
        super("Redisson Lock Failure");
    }
}
