package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SmsService {

    private final HttpClientProvider httpClient;

    public SmsService(HttpClientProvider httpClient) {
        this.httpClient = httpClient;
    }

    public ApiResponse sendSms(String phoneNumber, String senderId, String message) {
        Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        Objects.requireNonNull(senderId, "Sender ID cannot be null");
        Objects.requireNonNull(message, "Message cannot be null");

        Map<String, String> request = new HashMap<>();
        request.put("phone_number", phoneNumber);
        request.put("sender_id", senderId);
        request.put("message", message);

        return httpClient.post("/sms", request, ApiResponse.class);
    }
}
