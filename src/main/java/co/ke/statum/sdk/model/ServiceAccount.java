package co.ke.statum.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServiceAccount(
        @JsonProperty("account") String account,
        @JsonProperty("service_name") String serviceName) {
}
