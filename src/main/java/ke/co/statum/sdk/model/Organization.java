package ke.co.statum.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Organization(
        @JsonProperty("name") String name,
        @JsonProperty("details") OrganizationDetails details,
        @JsonProperty("accounts") List<ServiceAccount> accounts) {
}
