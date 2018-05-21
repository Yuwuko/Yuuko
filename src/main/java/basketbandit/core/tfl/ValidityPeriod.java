package basketbandit.core.tfl;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "$type",
        "fromDate",
        "toDate",
        "isNow"
})

public class ValidityPeriod {

    @JsonProperty("$type")
    private String $type;

    @JsonProperty("fromDate")
    private String fromDate;

    @JsonProperty("toDate")
    private String toDate;

    @JsonProperty("isNow")
    private Boolean isNow;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("$type")
    public String get$type() {
        return $type;
    }

    @JsonProperty("$type")
    public void set$type(String $type) {
        this.$type = $type;
    }

    public ValidityPeriod with$type(String $type) {
        this.$type = $type;
        return this;
    }

    @JsonProperty("fromDate")
    public String getFromDate() {
        return fromDate;
    }

    @JsonProperty("fromDate")
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public ValidityPeriod withFromDate(String fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    @JsonProperty("toDate")
    public String getToDate() {
        return toDate;
    }

    @JsonProperty("toDate")
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public ValidityPeriod withToDate(String toDate) {
        this.toDate = toDate;
        return this;
    }

    @JsonProperty("isNow")
    public Boolean getIsNow() {
        return isNow;
    }

    @JsonProperty("isNow")
    public void setIsNow(Boolean isNow) {
        this.isNow = isNow;
    }

    public ValidityPeriod withIsNow(Boolean isNow) {
        this.isNow = isNow;
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

    public ValidityPeriod withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}