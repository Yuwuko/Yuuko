package com.yuuko.core.modules.world.tfl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "reason",
        "statusSeverityDescription"
})
@JsonIgnoreProperties(ignoreUnknown = true)
class LineStatus {

    @JsonProperty("reason")
    private String reason;
    @JsonProperty("statusSeverityDescription")
    private String statusSeverityDescription;

    @JsonProperty("reason")
    String getReason() { return reason; }

    @JsonProperty("statusSeverityDescription")
    String getStatusSeverityDescription() {
        return statusSeverityDescription;
    }
}
