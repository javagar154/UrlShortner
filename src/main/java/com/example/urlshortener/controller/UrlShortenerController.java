package com.example.urlshortener.controller;

import com.example.urlshortener.model.ShortenRequest;
import com.example.urlshortener.model.ShortenResponse;
import com.example.urlshortener.model.ErrorResponse;
import com.example.urlshortener.service.UrlShortenerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller — two endpoints:
 *   POST /shorten   → accepts a long URL, returns a short code
 *   GET  /{shortCode} → redirects to the original URL (or 404)
 */
@RestController
public class UrlShortenerController {

    private final UrlShortenerService service;

    // Constructor injection — preferred over @Autowired field injection
    public UrlShortenerController(UrlShortenerService service) {
        this.service = service;
    }

    /**
     * Shorten a URL.
     *
     * @Valid triggers Jakarta Validation on ShortenRequest.
     * If validation fails, Spring automatically returns 400 via the
     * GlobalExceptionHandler before this method body is ever reached.
     *
     * HTTP 200 OK is appropriate here: the resource was processed successfully
     * and a code (new or existing) is returned.
     */
    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        String code = service.shorten(request.getUrl());
        return ResponseEntity.ok(new ShortenResponse(code));
    }

    /**
     * Redirect to the original URL.
     *
     * HTTP 302 Found is the standard status for a temporary redirect.
     * The Location header tells the client (browser or Postman) where to go.
     * A 301 (permanent) would be wrong here because in-memory storage is not
     * permanent and codes could theoretically be reassigned after a restart.
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirect(@PathVariable String shortCode) {
        String longUrl = service.getLongUrl(shortCode);

        if (longUrl == null) {
            // Short code not found — return a structured 404 body
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Short code not found: " + shortCode));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, longUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302
    }
}
