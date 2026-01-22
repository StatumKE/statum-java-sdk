package ke.co.statum.sdk.exceptions;

/**
 * Thrown when a network error occurs (e.g., timeout, connection refused).
 */
public class NetworkException extends ApiException {
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
