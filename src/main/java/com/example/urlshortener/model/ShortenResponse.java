package com.example.urlshortener.model;

/**
 * Response body for POST /shorten.
 * Returns the generated (or existing) short code for the submitted URL.
 */
public class ShortenResponse {

    private String shortCode;

    public ShortenResponse() {}

    public ShortenResponse(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}
