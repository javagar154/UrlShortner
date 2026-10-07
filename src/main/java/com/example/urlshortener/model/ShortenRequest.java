package com.example.urlshortener.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body for POST /shorten.
 * Validation annotations ensure we reject bad input before it reaches the service.
 */
public class ShortenRequest {

    /**
     * @NotBlank  - rejects null, empty string, and whitespace-only strings.
     * @Pattern   - ensures the value looks like a valid HTTP/HTTPS URL.
     *              A proper URL validator library would be more robust in production,
     *              but this regex is sufficient for the challenge scope.
     */
    @NotBlank(message = "URL must not be blank")
    @Pattern(
        regexp = "^(https?://).+",
        message = "URL must start with http:// or https://"
    )
    private String url;

    // Default constructor required by Jackson for JSON deserialization
    public ShortenRequest() {}

    public ShortenRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
