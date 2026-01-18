package co.ke.statum.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generic API response for operations like Send Airtime/SMS.
 */
public record ApiResponse(
        @JsonProperty("status_code") int statusCode,
        @JsonProperty("description") String description,
        @JsonProperty("request_id") String requestId) {
}
