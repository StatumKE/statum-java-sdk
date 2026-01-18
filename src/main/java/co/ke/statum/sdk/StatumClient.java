package co.ke.statum.sdk;

import co.ke.statum.sdk.config.StatumConfig;
import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.services.AccountService;
import co.ke.statum.sdk.services.AirtimeService;
import co.ke.statum.sdk.services.SmsService;

import java.util.Objects;

/**
 * Main entry point for the Statum SDK.
 * 
 * <p>
 * This client provides access to all Statum API services including Airtime,
 * SMS, and Account Management.
 * The client is thread-safe and should be reused across your application to
 * avoid unnecessary overhead.
 * </p>
 * 
 * <h2>Basic Usage</h2>
 * 
 * <pre>{@code
 * // Initialize the client
 * StatumConfig config = new StatumConfig("consumerKey", "consumerSecret");
 * StatumClient client = new StatumClient(config);
 * 
 * // Send airtime
 * ApiResponse response = client.getAirtimeService()
 *         .sendAirtime("254712345678", "100");
 * 
 * // Send SMS
 * ApiResponse sms = client.getSmsService()
 *         .sendSms("254712345678", "SENDER", "Hello!");
 * 
 * // Get account details
 * AccountDetailsResponse account = client.getAccountService()
 *         .getAccountDetails();
 * }</pre>
 * 
 * <h2>Thread Safety</h2>
 * <p>
 * This class is fully thread-safe. You can safely share a single instance
 * across multiple threads.
 * It is recommended to create a single instance and reuse it throughout your
 * application:
 * </p>
 * 
 * <pre>{@code
 * // Good: Create once, reuse everywhere
 * public class Application {
 *     private static final StatumClient CLIENT = new StatumClient(config);
 *     public static StatumClient getClient() { return CLIENT; }
 * }
 * 
 * // Bad: Creating new instances repeatedly (expensive!)
 * public void processPayment() {
 *     StatumClient client = new StatumClient(config); // Don't do this!
 *     client.getAirtimeService().sendAirtime(...);
 * }
 * }</pre>
 * 
 * <h2>Error Handling</h2>
 * <p>
 * All service methods may throw subclasses of {@code ApiException}:
 * </p>
 * <ul>
 * <li>{@code AuthenticationException} - Invalid credentials (HTTP 401)</li>
 * <li>{@code AuthorizationException} - Access denied (HTTP 403)</li>
 * <li>{@code ValidationException} - Invalid request data (HTTP 422)</li>
 * <li>{@code NetworkException} - Network or timeout errors</li>
 * <li>{@code ApiException} - Other API errors</li>
 * </ul>
 * 
 * @see StatumConfig
 * @see AirtimeService
 * @see SmsService
 * @see AccountService
 * @since 1.0.0
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
