package ke.co.statum.sdk.http;

import ke.co.statum.sdk.config.StatumConfig;
import ke.co.statum.sdk.exceptions.ValidationException;
import ke.co.statum.sdk.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpClientProviderBugFixTest {

    private StatumConfig config;
    private HttpClient mockHttpClient;
    private HttpClientProvider httpClientProvider;

    @BeforeEach
    void setUp() {
        config = new StatumConfig("testKey", "testSecret");
        mockHttpClient = mock(HttpClient.class);
        httpClientProvider = new HttpClientProvider(config, mockHttpClient);
    }

    @Test
    void shouldHandle422WithMixedTypesWithoutCrashing() throws IOException, InterruptedException {
        // Simulating the response described by the user: status_code (Integer) + error
        // map
        String json = """
                {
                    "status_code": 422,
                    "description": "Validation failed",
                    "validation_errors": {
                        "sender_id": ["Unauthorized"],
                        "status_code": 422
                    }
                }
                """;

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(422);
        when(mockResponse.body()).thenReturn(json);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            httpClientProvider.get("/test", ApiResponse.class);
        });

        assertEquals(422, exception.getStatusCode());
        Map<String, List<String>> errors = exception.getValidationErrors();

        // Should contain sender_id (which is a list)
        assertTrue(errors.containsKey("sender_id"));
        assertEquals(List.of("Unauthorized"), errors.get("sender_id"));

        // Should NOT contain status_code (which is an Integer, not a list)
        assertFalse(errors.containsKey("status_code"));
    }

    @Test
    void shouldHandle422WithErrorsAtRootWithoutCrashing() throws IOException, InterruptedException {
        // Simulating errors at the root with mixed types
        String json = """
                {
                    "status_code": 422,
                    "description": "Validation failed",
                    "phone_number": ["Invalid format"]
                }
                """;

        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(422);
        when(mockResponse.body()).thenReturn(json);
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            httpClientProvider.get("/test", ApiResponse.class);
        });

        Map<String, List<String>> errors = exception.getValidationErrors();
        assertTrue(errors.containsKey("phone_number"));
        assertEquals(List.of("Invalid format"), errors.get("phone_number"));
        assertFalse(errors.containsKey("status_code"));
    }
}
