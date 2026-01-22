package ke.co.statum.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrganizationDetails(
        @JsonProperty("available_balance") double availableBalance,
        @JsonProperty("location") String location,
        @JsonProperty("website") String website,
        @JsonProperty("office_email") String officeEmail,
        @JsonProperty("office_mobile") String officeMobile,
        @JsonProperty("mpesa_account_top_up_code") String mpesaAccountTopUpCode) {
}
