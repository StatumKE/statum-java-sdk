package ke.co.statum.sdk.services;

import ke.co.statum.sdk.http.HttpClientProvider;
import ke.co.statum.sdk.model.AccountDetailsResponse;

public class AccountService {

    private final HttpClientProvider httpClient;

    public AccountService(HttpClientProvider httpClient) {
        this.httpClient = httpClient;
    }

    public AccountDetailsResponse getAccountDetails() {
        return httpClient.get("/account-details", AccountDetailsResponse.class);
    }
}
