package com.khambhatpragati.model.response;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public class GenericResponse {
    private boolean status;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}