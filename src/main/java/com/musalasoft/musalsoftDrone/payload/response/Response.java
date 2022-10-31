package com.musalasoft.musalsoftDrone.payload.response;

import java.util.HashMap;
import java.util.Map;

public class Response extends HashMap<String,Object> {

    enum Status {
        Error,
        Success

    }

    public Response(Status status) {
        super();
        super.put("status", status.name());
        super.put("data", data);
    }



    public void setMessage(String message) {
        super.put("message", message);
    }
    public String  getMessage() {
        return (String) this.get("message");
    }

    public String  getStatus() {
        return (String) this.get("status");
    }

    public Map<String ,Object> getData() {
        return data;
    }



    private final Map<String, Object> data = new HashMap<>();

    public void addDataValue(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    public Map<String, Object> toMap() {
        return this;
    }

}
