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
        "name",
        "modeName",
        "disruptions",
        "created",
        "modified",
        "lineStatuses",
        "routeSections",
        "serviceTypes",
        "crowding"
})

public class LineManager {

    @JsonProperty("$type")
    private String $type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("modeName")
    private String modeName;

    @JsonProperty("disruptions")
    private List<Object> disruptions = new ArrayList<>();

    @JsonProperty("created")
    private String created;

    @JsonProperty("modified")
    private String modified;

    @JsonProperty("lineStatuses")
    private List<LineStatus> lineStatuses = new ArrayList<>();

    @JsonProperty("routeSections")
    private List<Object> routeSections = new ArrayList<>();

    @JsonProperty("serviceTypes")
    private List<ServiceType> serviceTypes = new ArrayList<>();

    @JsonProperty("crowding")
    private Crowding crowding;

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

    public LineManager with$type(String $type) {
        this.$type = $type;
        return this;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public LineManager withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public LineManager withName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("modeName")
    public String getModeName() {
        return modeName;
    }

    @JsonProperty("modeName")
    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public LineManager withModeName(String modeName) {
        this.modeName = modeName;
        return this;
    }

    @JsonProperty("disruptions")
    public List<Object> getDisruptions() {
        return disruptions;
    }

    @JsonProperty("disruptions")
    public void setDisruptions(List<Object> disruptions) {
        this.disruptions = disruptions;
    }

    public LineManager withDisruptions(List<Object> disruptions) {
        this.disruptions = disruptions;
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

    public LineManager withCreated(String created) {
        this.created = created;
        return this;
    }

    @JsonProperty("modified")
    public String getModified() {
        return modified;
    }

    @JsonProperty("modified")
    public void setModified(String modified) {
        this.modified = modified;
    }

    public LineManager withModified(String modified) {
        this.modified = modified;
        return this;
    }

    @JsonProperty("lineStatuses")
    public List<LineStatus> getLineStatuses() {
        return lineStatuses;
    }

    @JsonProperty("lineStatuses")
    public void setLineStatuses(List<LineStatus> lineStatuses) {
        this.lineStatuses = lineStatuses;
    }

    public LineManager withLineStatuses(List<LineStatus> lineStatuses) {
        this.lineStatuses = lineStatuses;
        return this;
    }

    @JsonProperty("routeSections")
    public List<Object> getRouteSections() {
        return routeSections;
    }

    @JsonProperty("routeSections")
    public void setRouteSections(List<Object> routeSections) {
        this.routeSections = routeSections;
    }

    public LineManager withRouteSections(List<Object> routeSections) {
        this.routeSections = routeSections;
        return this;
    }

    @JsonProperty("serviceTypes")
    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    @JsonProperty("serviceTypes")
    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public LineManager withServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
        return this;
    }

    @JsonProperty("crowding")
    public Crowding getCrowding() {
        return crowding;
    }

    @JsonProperty("crowding")
    public void setCrowding(Crowding crowding) {
        this.crowding = crowding;
    }

    public LineManager withCrowding(Crowding crowding) {
        this.crowding = crowding;
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

    public LineManager withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
