package co.ke.statum.sdk;

import co.ke.statum.sdk.config.StatumConfig;
import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.services.AccountService;
import co.ke.statum.sdk.services.AirtimeService;
import co.ke.statum.sdk.services.SmsService;

import java.util.Objects;

/**
 * Main entry point for the Statum SDK.
 * This class is thread-safe and should be reused.
 */
public class StatumClient {

    private final AirtimeService airtimeService;
    private final SmsService smsService;
    private final AccountService accountService;

    public StatumClient(StatumConfig config) {
        Objects.requireNonNull(config, "Config must not be null");
        HttpClientProvider httpClient = new HttpClientProvider(config);

        this.airtimeService = new AirtimeService(httpClient);
        this.smsService = new SmsService(httpClient);
        this.accountService = new AccountService(httpClient);
    }

    public AirtimeService getAirtimeService() {
        return airtimeService;
    }

    public SmsService getSmsService() {
        return smsService;
    }

    public AccountService getAccountService() {
        return accountService;
    }
}
