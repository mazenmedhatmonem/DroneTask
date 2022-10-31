package com.musalasoft.musalsoftDrone.payload.response;

public class SuccessResponse extends Response{
    public SuccessResponse() {
        super(Status.Success);
        setMessage("Successful Response");
    }
}
