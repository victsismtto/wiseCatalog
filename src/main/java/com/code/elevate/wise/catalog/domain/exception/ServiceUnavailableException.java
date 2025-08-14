package com.code.elevate.wise.catalog.domain.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String error) {
        super(error);
    }
}
