package examples;

import co.ke.statum.sdk.StatumClient;
import co.ke.statum.sdk.config.StatumConfig;
import co.ke.statum.sdk.exceptions.*;
import co.ke.statum.sdk.model.ApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Demonstrates comprehensive error handling with the Statum SDK.
 */
public class ErrorHandlingExample {

    public static void main(String[] args) {
        StatumConfig config = new StatumConfig(
                System.getenv("STATUM_CONSUMER_KEY"),
                System.getenv("STATUM_CONSUMER_SECRET"));
        StatumClient client = new StatumClient(config);

        // Example 1: Handle all exception types
        sendAirtimeWithErrorHandling(client, "254712345678", "100");

        // Example 2: Handle validation errors
        handleValidationErrors(client);

        // Example 3: Retry on network errors
        sendWithRetry(client, "254712345678", "50", 3);
    }

    /**
     * Demonstrates handling all exception types.
     */
    private static void sendAirtimeWithErrorHandling(StatumClient client, String phone, String amount) {
        System.out.println("\n=== Comprehensive Error Handling ===");

        try {
            ApiResponse response = client.getAirtimeService()
                    .sendAirtime(phone, amount);

            System.out.println("✅ Success! Request ID: " + response.requestId());

        } catch (AuthenticationException e) {
            // 401 - Invalid credentials
            System.err.println("❌ Authentication Error:");
            System.err.println("   Message: " + e.getMessage());
            System.err.println("   Solution: Check your consumer key and secret");
            System.err.println("   Status Code: " + e.getStatusCode());

        } catch (AuthorizationException e) {
            // 403 - Insufficient permissions
            System.err.println("❌ Authorization Error:");
            System.err.println("   Message: " + e.getMessage());
            System.err.println("   Solution: Verify your account has the required permissions");

        } catch (ValidationException e) {
            // 422 - Invalid input
            System.err.println("❌ Validation Error:");
            System.err.println("   Message: " + e.getMessage());

            Map<String, List<String>> errors = e.getValidationErrors();
            if (errors != null && !errors.isEmpty()) {
                System.err.println("   Field Errors:");
                errors.forEach((field, messages) -> {
                    System.err.println("     - " + field + ": " + String.join(", ", messages));
                });
            }

        } catch (NetworkException e) {
            // Network/timeout errors
            System.err.println("❌ Network Error:");
            System.err.println("   Message: " + e.getMessage());
            System.err.println("   Solution: Check your internet connection and retry");
            if (e.getCause() != null) {
                System.err.println("   Cause: " + e.getCause().getClass().getSimpleName());
            }

        } catch (ApiException e) {
            // Other API errors (4xx/5xx)
            System.err.println("❌ API Error:");
            System.err.println("   Message: " + e.getMessage());
            System.err.println("   Status Code: " + e.getStatusCode());
            System.err.println("   Response Body: " + e.getResponseBody());
        }
    }

    /**
     * Demonstrates handling validation errors with invalid data.
     */
    private static void handleValidationErrors(StatumClient client) {
        System.out.println("\n=== Validation Error Handling ===");

        try {
            // Intentionally send invalid data
            client.getAirtimeService().sendAirtime("invalid-phone", "-100");

        } catch (ValidationException e) {
            System.err.println("❌ Validation failed as expected:");

            Map<String, List<String>> errors = e.getValidationErrors();

            // Check specific fields
            if (errors != null) {
                if (errors.containsKey("phone_number")) {
                    System.err.println("   Phone number errors:");
                    errors.get("phone_number").forEach(msg -> System.err.println("     - " + msg));
                }

                if (errors.containsKey("amount")) {
                    System.err.println("   Amount errors:");
                    errors.get("amount").forEach(msg -> System.err.println("     - " + msg));
                }
            }

        } catch (ApiException e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates retry logic for network errors.
     */
    private static void sendWithRetry(StatumClient client, String phone, String amount, int maxRetries) {
        System.out.println("\n=== Retry Logic Example ===");

        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                ApiResponse response = client.getAirtimeService()
                        .sendAirtime(phone, amount);

                System.out.println("✅ Success on attempt " + (attempt + 1));
                System.out.println("   Request ID: " + response.requestId());
                return;

            } catch (NetworkException e) {
                attempt++;
                System.err.println("⚠️  Network error on attempt " + attempt + "/" + maxRetries);

                if (attempt >= maxRetries) {
                    System.err.println("❌ Max retries exceeded. Giving up.");
                    throw e;
                }

                // Exponential backoff: 1s, 2s, 4s, ...
                long delaySecs = (long) Math.pow(2, attempt - 1);
                System.out.println("   Retrying in " + delaySecs + " seconds...");

                try {
                    Thread.sleep(delaySecs * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }

            } catch (ApiException e) {
                // Don't retry on API errors (auth, validation, etc.)
                System.err.println("❌ API error (not retrying): " + e.getMessage());
                throw e;
            }
        }
    }
}
