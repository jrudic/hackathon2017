package com.projectx.jovanrudic.mhydrabanking.model;

/**
 * Created by jovanrudic on 7/1/17.
 */

public class ResponseMessage {
    int error;
    String errorMessage;

    public ResponseMessage(int error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
