package com.toy.webshop.exception;

public class NotExistItemException extends RuntimeException{

    public NotExistItemException() {
        super();
    }

    public NotExistItemException(String message) {
        super(message);
    }

    public NotExistItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistItemException(Throwable cause) {
        super(cause);
    }
}
