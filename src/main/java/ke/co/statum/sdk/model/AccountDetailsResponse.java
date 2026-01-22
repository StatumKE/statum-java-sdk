package ke.co.statum.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountDetailsResponse(
                @JsonProperty("status_code") int statusCode,
                @JsonProperty("description") String description,
                @JsonProperty("request_id") String requestId,
                @JsonProperty("organization") Organization organization) {
}
