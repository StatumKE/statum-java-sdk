package co.ke.statum.sdk.http;

import co.ke.statum.sdk.config.StatumConfig;
import co.ke.statum.sdk.exceptions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Internal HTTP client wrapper.
 */
public class HttpClientProvider {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final StatumConfig config;
    private final String authHeader;

    public HttpClientProvider(StatumConfig config) {
        this(config, HttpClient.newBuilder()
                .connectTimeout(config.getTimeout())
                .build());
    }

    public HttpClientProvider(StatumConfig config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE); // API uses snake_case

        String credentials = config.getConsumerKey() + ":" + config.getConsumerSecret();
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBaseUrl() + endpoint))
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return sendRequest(request, responseType);
        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to serialize request body", e);
        }
    }

    public <T> T get(String endpoint, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.getBaseUrl() + endpoint))
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .GET()
                .build();

        return sendRequest(request, responseType);
    }

    private <T> T sendRequest(HttpRequest request, Class<T> responseType) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            handleError(response);
            return objectMapper.readValue(response.body(), responseType);
        } catch (IOException | InterruptedException e) {
            throw new NetworkException("Request failed", e);
        }
    }

    private void handleError(HttpResponse<String> response) {
        int status = response.statusCode();
        if (status >= 200 && status < 300) {
            return;
        }

        String body = response.body();
        if (status == 401) {
            throw new AuthenticationException("Authentication failed", body);
        } else if (status == 403) {
            throw new AuthorizationException("Access denied", body);
        } else if (status == 422) {
            // Try to parse validation errors if possible, otherwise generic map
            Map<String, List<String>> errors = null;
            try {
                // Assuming validation errors come in a specific format like {"errors":
                // {"field": ["msg"]}}
                // For now keeping it simple or parsing as generic Map if structure is known
                // If the API returns a map of field -> list of errors directly:
                @SuppressWarnings("unchecked")
                Map<String, List<String>> parsedErrors = objectMapper.readValue(body, Map.class);
                errors = parsedErrors;
            } catch (Exception ignored) {
            }
            throw new ValidationException("Validation failed", body, errors);
        } else {
            throw new ApiException("API error: " + status, status, body);
        }
    }
}
