package ke.co.statum.sdk.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration for the Statum SDK.
 * 
 * <p>
 * This class holds all configuration parameters needed to connect to the Statum
 * API,
 * including authentication credentials, base URL, and timeout settings.
 * </p>
 * 
 * <h2>Basic Configuration</h2>
 * 
 * <pre>{@code
 * // Simple configuration with defaults
 * StatumConfig config = new StatumConfig("consumerKey", "consumerSecret");
 * }</pre>
 * 
 * <h2>Advanced Configuration</h2>
 * 
 * <pre>{@code
 * // Custom base URL and timeout
 * StatumConfig config = new StatumConfig(
 *         "consumerKey",
 *         "consumerSecret",
 *         "https://api.statum.co.ke/api/v2",
 *         Duration.ofSeconds(60));
 * }</pre>
 * 
 * <h2>Security Best Practices</h2>
 * <ul>
 * <li>Never hardcode credentials - use environment variables or secrets
 * management</li>
 * <li>Credentials are never logged by the SDK</li>
 * <li>All API calls use HTTPS by default</li>
 * </ul>
 * 
 * <pre>{@code
 * // Good: Load from environment
 * StatumConfig config = new StatumConfig(
 *         System.getenv("STATUM_CONSUMER_KEY"),
 *         System.getenv("STATUM_CONSUMER_SECRET"));
 * }</pre>
 * 
 * @since 1.0.0
 */
public class StatumConfig {
    private static final String DEFAULT_BASE_URL = "https://api.statum.co.ke/api/v2";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    private final String consumerKey;
    private final String consumerSecret;
    private final String baseUrl;
    private final Duration timeout;

    public StatumConfig(String consumerKey, String consumerSecret) {
        this(consumerKey, consumerSecret, DEFAULT_BASE_URL, DEFAULT_TIMEOUT);
    }

    public StatumConfig(String consumerKey, String consumerSecret, String baseUrl, Duration timeout) {
        this.consumerKey = Objects.requireNonNull(consumerKey, "Consumer Key must not be null");
        this.consumerSecret = Objects.requireNonNull(consumerSecret, "Consumer Secret must not be null");
        this.baseUrl = Objects.requireNonNull(baseUrl, "Base URL must not be null");
        this.timeout = Objects.requireNonNull(timeout, "Timeout must not be null");
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Duration getTimeout() {
        return timeout;
    }
}
