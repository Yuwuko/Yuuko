package com.yuuko.core.modules.world.tfl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.yuuko.core.utilities.TextUtility;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "lineStatuses"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineManager {

    @JsonProperty("name")
    private String name;
    @JsonProperty("lineStatuses")
    private List<LineStatus> lineStatuses = new ArrayList<>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("lineStatuses")
    public String getLineStatusString() {
        StringBuilder statuses = new StringBuilder();

        for(LineStatus line: lineStatuses) {
            statuses.append(line.getStatusSeverityDescription()).append("\n");
        }

        int index = statuses.lastIndexOf("\n");
        statuses.replace(index, index + 1, "");

        return statuses.toString();
    }

    public String getLineStatusReason() {
        StringBuilder reasons = new StringBuilder();
        String previous = "";

        for(LineStatus line: lineStatuses) {
            if(line.getReason() != null && !previous.equals(name)) {
                reasons.append(line.getReason()).append("\n\n");
                previous = name;
            }
        }
        TextUtility.removeLastOccurrence(reasons, "\n\n");

        return reasons.toString().replace("e:", "e**:").replace("E:", "E**:");
    }

}
