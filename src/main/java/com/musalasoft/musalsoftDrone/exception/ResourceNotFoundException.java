package com.musalasoft.musalsoftDrone.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException() {
        super("resource not found");
    }

    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    public ResourceNotFoundException(Class<?> aClass) {
        super(String.format("%s not found", aClass.getSimpleName()));
    }
}
