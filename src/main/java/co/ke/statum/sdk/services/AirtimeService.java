package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class AirtimeService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?:\\+254|254|0)(7|1)[0-9]{8}$");
    private static final double MIN_AMOUNT = 5.0;
    private static final double MAX_AMOUNT = 10000.0;

    private final HttpClientProvider httpClient;

    public AirtimeService(HttpClientProvider httpClient) {
        this.httpClient = httpClient;
    }

    public ApiResponse sendAirtime(String phoneNumber, String amount) {
        Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");

        // Validate phone number format
        validatePhoneNumber(phoneNumber);

        // Validate amount
        validateAmount(amount);

        Map<String, String> request = new HashMap<>();
        request.put("phone_number", phoneNumber);
        request.put("amount", amount);

        return httpClient.post("/airtime", request, ApiResponse.class);
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException(
                    "Invalid phone number format. Expected Kenyan number like +254712345678, 254712345678, or 0712345678");
        }
    }

    private void validateAmount(String amount) {
        double value;
        try {
            value = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a valid number", e);
        }

        if (value < MIN_AMOUNT) {
            throw new IllegalArgumentException(
                    String.format("Amount must be at least KES %.2f (got: %.2f)", MIN_AMOUNT, value));
        }

        if (value > MAX_AMOUNT) {
            throw new IllegalArgumentException(
                    String.format("Amount cannot exceed KES %.2f (got: %.2f)", MAX_AMOUNT, value));
        }
    }
}
