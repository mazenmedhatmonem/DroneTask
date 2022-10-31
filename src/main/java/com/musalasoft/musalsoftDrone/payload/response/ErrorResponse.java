package com.musalasoft.musalsoftDrone.payload.response;

public class ErrorResponse extends Response{

    public ErrorResponse() {
        super(Status.Error);
        setMessage("Failed Response");
    }
}
