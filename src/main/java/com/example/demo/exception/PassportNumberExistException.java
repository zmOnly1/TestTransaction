package com.example.demo.exception;

/**
 * Created by zm on 2019/8/10.
 */
public class PassportNumberExistException extends RuntimeException {

    public PassportNumberExistException() {
        super();
    }

    public PassportNumberExistException(String message) {
        super(message);
    }
}
