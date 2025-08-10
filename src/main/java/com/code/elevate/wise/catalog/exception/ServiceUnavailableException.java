package com.code.elevate.wise.catalog.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String error) {
        super(error);
    }
}
