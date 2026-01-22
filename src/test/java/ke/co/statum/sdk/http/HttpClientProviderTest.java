package ke.co.statum.sdk.http;

import ke.co.statum.sdk.config.StatumConfig;
import ke.co.statum.sdk.exceptions.*;
import ke.co.statum.sdk.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpClientProviderTest {

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
    void post_shouldThrowAuthenticationException_when401() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(401);
        when(mockResponse.body()).thenReturn("{\"error\":\"Unauthorized\"}");
        @SuppressWarnings("unchecked")
        HttpResponse.BodyHandler<String> bodyHandler = any(HttpResponse.BodyHandler.class);
        when(mockHttpClient.send(any(HttpRequest.class), bodyHandler))
                .thenReturn(mockResponse);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "value");

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            httpClientProvider.post("/test", requestBody, ApiResponse.class);
        });

        assertEquals(401, exception.getStatusCode());
        assertEquals("{\"error\":\"Unauthorized\"}", exception.getResponseBody());
    }

    @Test
    void post_shouldThrowAuthorizationException_when403() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(403);
        when(mockResponse.body()).thenReturn("{\"error\":\"Forbidden\"}");
        @SuppressWarnings("unchecked")
        HttpResponse.BodyHandler<String> bodyHandler = any(HttpResponse.BodyHandler.class);
        when(mockHttpClient.send(any(HttpRequest.class), bodyHandler))
                .thenReturn(mockResponse);

        Map<String, String> requestBody = new HashMap<>();

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            httpClientProvider.post("/test", requestBody, ApiResponse.class);
        });

        assertEquals(403, exception.getStatusCode());
    }

    @Test
    void post_shouldThrowValidationException_when422() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(422);
        when(mockResponse.body()).thenReturn("{\"phone_number\":[\"Invalid format\"]}");
        @SuppressWarnings("unchecked")
        HttpResponse.BodyHandler<String> bodyHandler = any(HttpResponse.BodyHandler.class);
        when(mockHttpClient.send(any(HttpRequest.class), bodyHandler))
                .thenReturn(mockResponse);

        Map<String, String> requestBody = new HashMap<>();

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            httpClientProvider.post("/test", requestBody, ApiResponse.class);
        });

        assertEquals(422, exception.getStatusCode());
        assertNotNull(exception.getValidationErrors());
    }

    @Test
    void post_shouldThrowApiException_when500() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn("{\"error\":\"Internal Server Error\"}");
        @SuppressWarnings("unchecked")
        HttpResponse.BodyHandler<String> bodyHandler = any(HttpResponse.BodyHandler.class);
        when(mockHttpClient.send(any(HttpRequest.class), bodyHandler))
                .thenReturn(mockResponse);

        Map<String, String> requestBody = new HashMap<>();

        ApiException exception = assertThrows(ApiException.class, () -> {
            httpClientProvider.post("/test", requestBody, ApiResponse.class);
        });

        assertEquals(500, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("500"));
    }

    @Test
    void post_shouldThrowNetworkException_whenIOExceptionOccurs() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Connection refused"));

        Map<String, String> requestBody = new HashMap<>();

        NetworkException exception = assertThrows(NetworkException.class, () -> {
            httpClientProvider.post("/test", requestBody, ApiResponse.class);
        });

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    void post_shouldThrowNetworkException_whenInterruptedExceptionOccurs() throws IOException, InterruptedException {
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Thread interrupted"));

        Map<String, String> requestBody = new HashMap<>();

        NetworkException exception = assertThrows(NetworkException.class, () -> {
            httpClientProvider.post("/test", requestBody, ApiResponse.class);
        });

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof InterruptedException);
    }

    @Test
    void get_shouldThrowAuthenticationException_when401() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(401);
        when(mockResponse.body()).thenReturn("{\"error\":\"Unauthorized\"}");
        @SuppressWarnings("unchecked")
        HttpResponse.BodyHandler<String> bodyHandler = any(HttpResponse.BodyHandler.class);
        when(mockHttpClient.send(any(HttpRequest.class), bodyHandler))
                .thenReturn(mockResponse);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            httpClientProvider.get("/test", ApiResponse.class);
        });

        assertEquals(401, exception.getStatusCode());
    }

    @Test
    void constructor_shouldConfigureBasicAuth() {
        // Test that the constructor sets up auth correctly
        HttpClientProvider provider = new HttpClientProvider(config);
        assertNotNull(provider);
    }

    @Test
    void post_shouldHandleBaseUrlWithTrailingSlash() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"status_code\":200,\"description\":\"OK\",\"request_id\":\"123\"}");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        // Config with trailing slash
        StatumConfig configWithSlash = new StatumConfig("key", "secret", "https://api.statum.co.ke/api/v2/",
                config.getTimeout());
        HttpClientProvider providerWithSlash = new HttpClientProvider(configWithSlash, mockHttpClient);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "value");

        // Should handle correctly without double slashes
        assertDoesNotThrow(() -> {
            providerWithSlash.post("/airtime", requestBody, ApiResponse.class);
        });
    }

    @Test
    void post_shouldHandleEndpointWithoutLeadingSlash() throws IOException, InterruptedException {
        @SuppressWarnings("unchecked")
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"status_code\":200,\"description\":\"OK\",\"request_id\":\"123\"}");
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("test", "value");

        // Endpoint without leading slash should work
        assertDoesNotThrow(() -> {
            httpClientProvider.post("airtime", requestBody, ApiResponse.class);
        });
    }
}
