package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.ApiResponse;
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
class SmsServiceTest {

    @Mock
    private HttpClientProvider httpClient;

    private SmsService smsService;

    @BeforeEach
    void setUp() {
        smsService = new SmsService(httpClient);
    }

    @Test
    void sendSms_shouldReturnApiResponse_whenRequestIsSuccess() {
        ApiResponse mockResponse = new ApiResponse(200, "Submitted", "req-456");
        when(httpClient.post(eq("/sms"), any(), eq(ApiResponse.class))).thenReturn(mockResponse);

        ApiResponse response = smsService.sendSms("254712345678", "STATUM", "Hello World");

        assertNotNull(response);
        assertEquals(200, response.statusCode());
        assertEquals("Submitted", response.description());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String>> captor = ArgumentCaptor.forClass(Map.class);
        verify(httpClient).post(eq("/sms"), captor.capture(), eq(ApiResponse.class));

        Map<String, String> capturedRequest = captor.getValue();
        assertEquals("254712345678", capturedRequest.get("phone_number"));
        assertEquals("STATUM", capturedRequest.get("sender_id"));
        assertEquals("Hello World", capturedRequest.get("message"));
    }

    @Test
    void sendSms_shouldThrowException_whenPhoneNumberIsNull() {
        assertThrows(NullPointerException.class, () -> {
            smsService.sendSms(null, "SENDER", "Message");
        });
    }

    @Test
    void sendSms_shouldThrowException_whenSenderIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            smsService.sendSms("254712345678", null, "Message");
        });
    }

    @Test
    void sendSms_shouldThrowException_whenMessageIsNull() {
        assertThrows(NullPointerException.class, () -> {
            smsService.sendSms("254712345678", "SENDER", null);
        });
    }

    @Test
    void sendSms_shouldThrowException_whenPhoneNumberIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            smsService.sendSms("123456", "SENDER", "Message");
        });
    }

    @Test
    void sendSms_shouldThrowException_whenMessageIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            smsService.sendSms("254712345678", "SENDER", "");
        });
    }

    @Test
    void sendSms_shouldThrowException_whenMessageIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            smsService.sendSms("254712345678", "SENDER", "   ");
        });
    }

    @Test
    void sendSms_shouldAcceptVariousPhoneFormats() {
        ApiResponse mockResponse = new ApiResponse(200, "Submitted", "req-456");
        when(httpClient.post(eq("/sms"), any(), eq(ApiResponse.class))).thenReturn(mockResponse);

        // Test +254 format
        assertDoesNotThrow(() -> smsService.sendSms("+254712345678", "SENDER", "Test"));

        // Test 254 format
        assertDoesNotThrow(() -> smsService.sendSms("254712345678", "SENDER", "Test"));

        // Test 0 format
        assertDoesNotThrow(() -> smsService.sendSms("0712345678", "SENDER", "Test"));
    }
}
