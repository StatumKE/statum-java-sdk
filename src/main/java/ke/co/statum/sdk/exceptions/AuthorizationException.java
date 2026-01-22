package ke.co.statum.sdk.exceptions;

/**
 * Thrown when the user is not authorized to access the resource (HTTP 403).
 */
public class AuthorizationException extends ApiException {
    public AuthorizationException(String message, String responseBody) {
        super(message, 403, responseBody);
    }
}
