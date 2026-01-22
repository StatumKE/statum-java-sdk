package ke.co.statum.sdk.exceptions;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTests {

    @Test
    void apiException_shouldStoreStatusCodeAndBody() {
        ApiException exception = new ApiException("Test error", 500, "Response body");

        assertEquals("Test error", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertEquals("Response body", exception.getResponseBody());
    }

    @Test
    void apiException_shouldHandleNullBody() {
        ApiException exception = new ApiException("Test error", 500, null);

        assertEquals("Test error", exception.getMessage());
        assertEquals(500, exception.getStatusCode());
        assertNull(exception.getResponseBody());
    }

    @Test
    void authenticationException_shouldHave401StatusCode() {
        AuthenticationException exception = new AuthenticationException("Auth failed", "body");

        assertEquals(401, exception.getStatusCode());
        assertEquals("Auth failed", exception.getMessage());
        assertEquals("body", exception.getResponseBody());
    }

    @Test
    void authorizationException_shouldHave403StatusCode() {
        AuthorizationException exception = new AuthorizationException("Access denied", "body");

        assertEquals(403, exception.getStatusCode());
        assertEquals("Access denied", exception.getMessage());
    }

    @Test
    void validationException_shouldStoreValidationErrors() {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("phone_number", List.of("Invalid format", "Required"));
        errors.put("amount", List.of("Must be positive"));

        ValidationException exception = new ValidationException("Validation failed", "body", errors);

        assertEquals(422, exception.getStatusCode());
        assertEquals(2, exception.getValidationErrors().size());
        assertTrue(exception.getValidationErrors().containsKey("phone_number"));
        assertTrue(exception.getValidationErrors().containsKey("amount"));
        assertEquals(2, exception.getValidationErrors().get("phone_number").size());
    }

    @Test
    void validationException_shouldHandleNullErrors() {
        ValidationException exception = new ValidationException("Validation failed", "body", null);

        assertNotNull(exception.getValidationErrors());
        assertTrue(exception.getValidationErrors().isEmpty());
    }

    @Test
    void validationException_shouldBeImmutable() {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put("field", List.of("error"));

        ValidationException exception = new ValidationException("Validation failed", "body", errors);

        assertThrows(UnsupportedOperationException.class, () -> {
            exception.getValidationErrors().put("newField", List.of("newError"));
        });
    }

    @Test
    void networkException_shouldStoreCause() {
        Exception cause = new RuntimeException("Connection timeout");
        NetworkException exception = new NetworkException("Network error", cause);

        assertEquals("Network error", exception.getMessage());
        assertSame(cause, exception.getCause());
        assertEquals(0, exception.getStatusCode());
    }

    @Test
    void apiException_withMessage_shouldHaveZeroStatusCode() {
        ApiException exception = new ApiException("Generic error");

        assertEquals("Generic error", exception.getMessage());
        assertEquals(0, exception.getStatusCode());
        assertNull(exception.getResponseBody());
    }

    @Test
    void apiException_withCause_shouldStoreCause() {
        Exception cause = new RuntimeException("Root cause");
        ApiException exception = new ApiException("Wrapper error", cause);

        assertEquals("Wrapper error", exception.getMessage());
        assertSame(cause, exception.getCause());
        assertEquals(0, exception.getStatusCode());
    }
}
