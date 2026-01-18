package co.ke.statum.sdk;

import co.ke.statum.sdk.config.StatumConfig;
import co.ke.statum.sdk.services.AccountService;
import co.ke.statum.sdk.services.AirtimeService;
import co.ke.statum.sdk.services.SmsService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatumClientTest {

    @Test
    void constructor_shouldThrowException_whenConfigIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new StatumClient(null);
        });
    }

    @Test
    void constructor_shouldCreateServices_whenConfigIsValid() {
        StatumConfig config = new StatumConfig("key", "secret");
        StatumClient client = new StatumClient(config);

        assertNotNull(client.getAirtimeService());
        assertNotNull(client.getSmsService());
        assertNotNull(client.getAccountService());
    }

    @Test
    void getAirtimeService_shouldReturnSameInstance() {
        StatumConfig config = new StatumConfig("key", "secret");
        StatumClient client = new StatumClient(config);

        AirtimeService service1 = client.getAirtimeService();
        AirtimeService service2 = client.getAirtimeService();

        assertSame(service1, service2);
    }

    @Test
    void getSmsService_shouldReturnSameInstance() {
        StatumConfig config = new StatumConfig("key", "secret");
        StatumClient client = new StatumClient(config);

        SmsService service1 = client.getSmsService();
        SmsService service2 = client.getSmsService();

        assertSame(service1, service2);
    }

    @Test
    void getAccountService_shouldReturnSameInstance() {
        StatumConfig config = new StatumConfig("key", "secret");
        StatumClient client = new StatumClient(config);

        AccountService service1 = client.getAccountService();
        AccountService service2 = client.getAccountService();

        assertSame(service1, service2);
    }
}
