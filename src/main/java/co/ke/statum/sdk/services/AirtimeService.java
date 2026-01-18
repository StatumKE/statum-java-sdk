package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.ApiResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AirtimeService {

    private final HttpClientProvider httpClient;

    public AirtimeService(HttpClientProvider httpClient) {
        this.httpClient = httpClient;
    }

    public ApiResponse sendAirtime(String phoneNumber, String amount) {
        Objects.requireNonNull(phoneNumber, "Phone number cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");

        Map<String, String> request = new HashMap<>();
        request.put("phone_number", phoneNumber);
        request.put("amount", amount);

        return httpClient.post("/airtime", request, ApiResponse.class);
    }
}
