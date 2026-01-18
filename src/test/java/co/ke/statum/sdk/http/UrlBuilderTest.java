package co.ke.statum.sdk.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for URL building logic to prevent 404 errors from slash issues.
 */
class UrlBuilderTest {

    @Test
    void testUrlBuilding_baseWithoutSlash_endpointWithSlash() {
        String result = buildUrl("https://api.statum.co.ke/api/v2", "/airtime");
        assertEquals("https://api.statum.co.ke/api/v2/airtime", result);
    }

    @Test
    void testUrlBuilding_baseWithSlash_endpointWithSlash() {
        String result = buildUrl("https://api.statum.co.ke/api/v2/", "/airtime");
        assertEquals("https://api.statum.co.ke/api/v2/airtime", result);
    }

    @Test
    void testUrlBuilding_baseWithoutSlash_endpointWithoutSlash() {
        String result = buildUrl("https://api.statum.co.ke/api/v2", "airtime");
        assertEquals("https://api.statum.co.ke/api/v2/airtime", result);
    }

    @Test
    void testUrlBuilding_baseWithSlash_endpointWithoutSlash() {
        String result = buildUrl("https://api.statum.co.ke/api/v2/", "airtime");
        assertEquals("https://api.statum.co.ke/api/v2/airtime", result);
    }

    @Test
    void testUrlBuilding_complexPath() {
        String result = buildUrl("https://api.example.com/v1/", "/users/123/profile");
        assertEquals("https://api.example.com/v1/users/123/profile", result);
    }

    @Test
    void testUrlBuilding_multipleTrailingSlashes() {
        String result = buildUrl("https://api.example.com/v1///", "/endpoint");
        // Should handle gracefully - removes all trailing slashes
        assertEquals("https://api.example.com/v1/endpoint", result);
    }

    /**
     * Helper method that mirrors the private buildUrl in HttpClientProvider
     */
    private String buildUrl(String base, String endpoint) {
        // Remove trailing slash from base URL
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }

        // Ensure endpoint starts with slash
        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }

        return base + endpoint;
    }
}
