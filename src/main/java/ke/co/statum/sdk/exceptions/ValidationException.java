package ke.co.statum.sdk.exceptions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Thrown when the request validation fails (HTTP 422).
 */
public class ValidationException extends ApiException {
    private final Map<String, List<String>> validationErrors;

    public ValidationException(String message, String responseBody, Map<String, List<String>> validationErrors) {
        super(message, 422, responseBody);
        this.validationErrors = validationErrors != null ? Collections.unmodifiableMap(validationErrors)
                : Collections.emptyMap();
    }

    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }
}
