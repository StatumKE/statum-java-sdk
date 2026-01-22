package ke.co.statum.sdk.services;

import ke.co.statum.sdk.http.HttpClientProvider;
import ke.co.statum.sdk.model.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirtimeServiceTest {

    @Mock
    private HttpClientProvider httpClient;

    private AirtimeService airtimeService;

    @BeforeEach
    void setUp() {
        airtimeService = new AirtimeService(httpClient);
    }

    @Test
    void sendAirtime_shouldReturnApiResponse_whenRequestIsSuccess() {
        ApiResponse mockResponse = new ApiResponse(200, "Success", "req-123");
        when(httpClient.post(eq("/airtime"), any(), eq(ApiResponse.class))).thenReturn(mockResponse);

        ApiResponse response = airtimeService.sendAirtime("254712345678", "100.00");

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals("Success", response.description());
        assertEquals("req-123", response.requestId());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(httpClient).post(eq("/airtime"), captor.capture(), eq(ApiResponse.class));

        Map<String, String> capturedRequest = captor.getValue();
        assertEquals("254712345678", capturedRequest.get("phone_number"));
        assertEquals("100.00", capturedRequest.get("amount"));
    }

    @Test
    void sendAirtime_shouldThrowException_whenPhoneNumberIsNull() {
        assertThrows(NullPointerException.class, () -> {
            airtimeService.sendAirtime(null, "100");
        });
    }

    @Test
    void sendAirtime_shouldThrowException_whenAmountIsNull() {
        assertThrows(NullPointerException.class, () -> {
            airtimeService.sendAirtime("254712345678", null);
        });
    }

    @Test
    void sendAirtime_shouldThrowException_whenPhoneNumberIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            airtimeService.sendAirtime("123456", "100");
        });
    }

    @Test
    void sendAirtime_shouldThrowException_whenAmountIsTooLow() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            airtimeService.sendAirtime("254712345678", "4");
        });
        assertTrue(exception.getMessage().contains("at least"));
    }

    @Test
    void sendAirtime_shouldThrowException_whenAmountIsTooHigh() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            airtimeService.sendAirtime("254712345678", "10001");
        });
        assertTrue(exception.getMessage().contains("cannot exceed"));
    }

    @Test
    void sendAirtime_shouldThrowException_whenAmountIsNotNumeric() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            airtimeService.sendAirtime("254712345678", "abc");
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }

    @Test
    void sendAirtime_shouldAcceptVariousPhoneFormats() {
        ApiResponse mockResponse = new ApiResponse(200, "Success", "req-123");
        when(httpClient.post(eq("/airtime"), any(), eq(ApiResponse.class))).thenReturn(mockResponse);

        // Test +254 format
        assertDoesNotThrow(() -> airtimeService.sendAirtime("+254712345678", "100"));

        // Test 254 format
        assertDoesNotThrow(() -> airtimeService.sendAirtime("254712345678", "100"));

        // Test 0 format
        assertDoesNotThrow(() -> airtimeService.sendAirtime("0712345678", "100"));

        // Test 01 prefix
        assertDoesNotThrow(() -> airtimeService.sendAirtime("0112345678", "100"));
    }
}
