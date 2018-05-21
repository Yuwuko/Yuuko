package basketbandit.core.tfl;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "$type",
        "id",
        "lineId",
        "statusSeverity",
        "statusSeverityDescription",
        "reason",
        "created",
        "validityPeriods",
        "disruption"
})

public class LineStatus {

    @JsonProperty("$type")
    private String $type;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("lineId")
    private String lineId;

    @JsonProperty("statusSeverity")
    private Integer statusSeverity;

    @JsonProperty("statusSeverityDescription")
    private String statusSeverityDescription;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("created")
    private String created;

    @JsonProperty("validityPeriods")
    private List<ValidityPeriod> validityPeriods = new ArrayList<>();

    @JsonProperty("disruption")
    private Disruption disruption;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("$type")
    public String get$type() {
        return $type;
    }

    @JsonProperty("$type")
    public void set$type(String $type) {
        this.$type = $type;
    }

    public LineStatus with$type(String $type) {
        this.$type = $type;
        return this;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public LineStatus withId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("lineId")
    public String getLineId() {
        return lineId;
    }

    @JsonProperty("lineId")
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public LineStatus withLineId(String lineId) {
        this.lineId = lineId;
        return this;
    }

    @JsonProperty("statusSeverity")
    public Integer getStatusSeverity() {
        return statusSeverity;
    }

    @JsonProperty("statusSeverity")
    public void setStatusSeverity(Integer statusSeverity) {
        this.statusSeverity = statusSeverity;
    }

    public LineStatus withStatusSeverity(Integer statusSeverity) {
        this.statusSeverity = statusSeverity;
        return this;
    }

    @JsonProperty("statusSeverityDescription")
    public String getStatusSeverityDescription() {
        return statusSeverityDescription;
    }

    @JsonProperty("statusSeverityDescription")
    public void setStatusSeverityDescription(String statusSeverityDescription) {
        this.statusSeverityDescription = statusSeverityDescription;
    }

    public LineStatus withStatusSeverityDescription(String statusSeverityDescription) {
        this.statusSeverityDescription = statusSeverityDescription;
        return this;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    public LineStatus withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    public LineStatus withCreated(String created) {
        this.created = created;
        return this;
    }

    @JsonProperty("validityPeriods")
    public List<ValidityPeriod> getValidityPeriods() {
        return validityPeriods;
    }

    @JsonProperty("validityPeriods")
    public void setValidityPeriods(List<ValidityPeriod> validityPeriods) {
        this.validityPeriods = validityPeriods;
    }

    public LineStatus withValidityPeriods(List<ValidityPeriod> validityPeriods) {
        this.validityPeriods = validityPeriods;
        return this;
    }

    @JsonProperty("disruption")
    public Disruption getDisruption() {
        return disruption;
    }

    @JsonProperty("disruption")
    public void setDisruption(Disruption disruption) {
        this.disruption = disruption;
    }

    public LineStatus withDisruption(Disruption disruption) {
        this.disruption = disruption;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public LineStatus withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}