package ke.co.statum.sdk.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountDetailsResponseTest {

    @Test
    void shouldDeserializeFromOrganizationField() throws Exception {
        String json = """
                {
                    "status_code": 200,
                    "description": "Success",
                    "request_id": "req-123",
                    "organization": {
                        "name": "Test Org",
                        "details": {
                            "available_balance": 100.50
                        },
                        "accounts": []
                    }
                }
                """;

        ObjectMapper mapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        AccountDetailsResponse response = mapper.readValue(json, AccountDetailsResponse.class);

        assertNotNull(response);
        assertNotNull(response.organization());
        assertEquals("Test Org", response.organization().name());
        assertEquals(100.50, response.organization().details().availableBalance());
    }
}
