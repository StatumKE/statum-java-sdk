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

        ArgumentCaptor<Map> captor = ArgumentCaptor.forClass(Map.class);
        verify(httpClient).post(eq("/airtime"), captor.capture(), eq(ApiResponse.class));

        Map<String, String> capturedRequest = captor.getValue();
        assertEquals("254712345678", capturedRequest.get("phone_number"));
        assertEquals("100.00", capturedRequest.get("amount"));
    }
}
