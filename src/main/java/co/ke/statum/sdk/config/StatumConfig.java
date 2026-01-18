package co.ke.statum.sdk.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Configuration for the Statum SDK.
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
