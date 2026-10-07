package com.example.urlshortener.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Core business logic for the URL shortener.
 *
 * Storage strategy — two HashMaps for O(1) lookups in both directions:
 *   longToShort : long URL  → short code  (prevents duplicate entries for the same URL)
 *   shortToLong : short code → long URL   (used during redirect lookup)
 */
@Service
public class UrlShortenerService {

    // Characters used when generating a short code
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 6;

    // SecureRandom provides better randomness than java.util.Random
    private final SecureRandom random = new SecureRandom();

    // In-memory bidirectional maps (no database needed for this challenge)
    private final Map<String, String> longToShort = new HashMap<>();
    private final Map<String, String> shortToLong = new HashMap<>();

    /**
     * Returns an existing short code if the URL has already been shortened,
     * otherwise generates a new unique code, stores the mapping, and returns it.
     *
     * Time complexity: O(1) average for map lookup and insert.
     *   Code generation loop is O(1) amortized — collisions are extremely rare
     *   with 62^6 ≈ 56 billion possible codes.
     *
     * @param longUrl the original URL to shorten
     * @return the short code (new or existing)
     */
    public String shorten(String longUrl) {
        // Duplicate prevention: return the existing code if the URL is already stored
        if (longToShort.containsKey(longUrl)) {
            return longToShort.get(longUrl);
        }

        // Generate a unique short code (retry on the rare collision)
        String code = generateUniqueCode();

        // Store both directions
        longToShort.put(longUrl, code);
        shortToLong.put(code, longUrl);

        return code;
    }

    /**
     * Looks up the original URL for a given short code.
     *
     * Time complexity: O(1) average.
     *
     * @param shortCode the 6-character code from the request path
     * @return the original long URL, or null if the code does not exist
     */
    public String getLongUrl(String shortCode) {
        return shortToLong.get(shortCode);
    }

    /**
     * Generates a random 6-character alphanumeric code and guarantees it is
     * not already in use by retrying until a free code is found.
     */
    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (shortToLong.containsKey(code)); // retry on collision
        return code;
    }

    /**
     * Builds a random CODE_LENGTH string from ALPHABET.
     */
    private String generateCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
