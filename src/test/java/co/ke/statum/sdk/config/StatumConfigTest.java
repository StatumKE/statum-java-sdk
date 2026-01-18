package co.ke.statum.sdk.config;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class StatumConfigTest {

    @Test
    void constructor_shouldThrowException_whenConsumerKeyIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new StatumConfig(null, "secret");
        });
    }

    @Test
    void constructor_shouldThrowException_whenConsumerSecretIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new StatumConfig("key", null);
        });
    }

    @Test
    void constructor_shouldThrowException_whenBaseUrlIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new StatumConfig("key", "secret", null, Duration.ofSeconds(30));
        });
    }

    @Test
    void constructor_shouldThrowException_whenTimeoutIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new StatumConfig("key", "secret", "https://api.statum.co.ke/api/v2", null);
        });
    }

    @Test
    void constructor_shouldUseDefaults_whenUsingBasicConstructor() {
        StatumConfig config = new StatumConfig("testKey", "testSecret");

        assertEquals("testKey", config.getConsumerKey());
        assertEquals("testSecret", config.getConsumerSecret());
        assertEquals("https://api.statum.co.ke/api/v2", config.getBaseUrl());
        assertEquals(Duration.ofSeconds(30), config.getTimeout());
    }

    @Test
    void constructor_shouldUseCustomValues_whenUsingFullConstructor() {
        String customUrl = "https://custom.api.com/v1";
        Duration customTimeout = Duration.ofSeconds(60);

        StatumConfig config = new StatumConfig("key", "secret", customUrl, customTimeout);

        assertEquals("key", config.getConsumerKey());
        assertEquals("secret", config.getConsumerSecret());
        assertEquals(customUrl, config.getBaseUrl());
        assertEquals(customTimeout, config.getTimeout());
    }

    @Test
    void getters_shouldReturnCorrectValues() {
        StatumConfig config = new StatumConfig("myKey", "mySecret");

        assertEquals("myKey", config.getConsumerKey());
        assertEquals("mySecret", config.getConsumerSecret());
        assertNotNull(config.getBaseUrl());
        assertNotNull(config.getTimeout());
    }
}
