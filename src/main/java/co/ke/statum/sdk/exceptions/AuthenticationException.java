package co.ke.statum.sdk.exceptions;

/**
 * Thrown when authentication fails (HTTP 401).
 */
public class AuthenticationException extends ApiException {
    public AuthenticationException(String message, String responseBody) {
        super(message, 401, responseBody);
    }
}
