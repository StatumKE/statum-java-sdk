package co.ke.statum.sdk.services;

import co.ke.statum.sdk.http.HttpClientProvider;
import co.ke.statum.sdk.model.AccountDetailsResponse;
import co.ke.statum.sdk.model.Organization;
import co.ke.statum.sdk.model.OrganizationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private HttpClientProvider httpClient;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(httpClient);
    }

    @Test
    void getAccountDetails_shouldReturnDetails_whenRequestIsSuccess() {
        OrganizationDetails details = new OrganizationDetails(500.0, "Nairobi", "statum.co.ke", "info@statum.co.ke",
                "0722000000", "123456");
        Organization org = new Organization("My Org", details, Collections.emptyList());
        AccountDetailsResponse mockResponse = new AccountDetailsResponse(200, "Success", "req-789", org);

        when(httpClient.get(eq("/account-details"), eq(AccountDetailsResponse.class))).thenReturn(mockResponse);

        AccountDetailsResponse response = accountService.getAccountDetails();

        assertNotNull(response);
        assertEquals("My Org", response.organization().name());
        assertEquals(500.0, response.organization().details().availableBalance());

        verify(httpClient).get(eq("/account-details"), eq(AccountDetailsResponse.class));
    }
}
