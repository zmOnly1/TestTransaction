package com.example.demo.exception;

/**
 * Created by zm on 2019/8/11.
 */
public class ExceedMaxRetryException extends RuntimeException {

    public ExceedMaxRetryException() {
        super();
    }

    public ExceedMaxRetryException(String message) {
        super(message);
    }
}
