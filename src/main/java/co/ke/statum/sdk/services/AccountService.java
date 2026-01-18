package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.AccountDetailsResponse;

public class AccountService {

    private final HttpClientProvider httpClient;

    public AccountService(HttpClientProvider httpClient) {
        this.httpClient = httpClient;
    }

    public AccountDetailsResponse getAccountDetails() {
        return httpClient.get("/account-details", AccountDetailsResponse.class);
    }
}
