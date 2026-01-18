package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class SmsService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(?:\\+254|254|0)(7|1)[0-9]{8}$");

    private final HttpClientProvider httpClient;

    public SmsService(HttpClientProvider httpClient) {
        this.httpClient = httpClient;
    }

    public ApiResponse sendSms(String phoneNumber, String senderId, String message) {
        Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        Objects.requireNonNull(senderId, "Sender ID cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");

        // Validate phone number format
        validatePhoneNumber(phoneNumber);

        Map<String, String> request = new HashMap<>();
        request.put("phone_number", phoneNumber);
        request.put("sender_id", senderId);
        request.put("message", message);

        return httpClient.post("/sms", request, ApiResponse.class);
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException(
                    "Invalid phone number format. Expected Kenyan number like +254712345678, 254712345678, or 0712345678");
        }
    }
}
