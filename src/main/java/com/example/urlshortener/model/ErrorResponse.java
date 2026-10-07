package com.example.urlshortener.model;

/**
 * Generic error response body used for 400 and 404 responses.
 * Keeps error output consistent across all failure cases.
 */
public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse() {}

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
