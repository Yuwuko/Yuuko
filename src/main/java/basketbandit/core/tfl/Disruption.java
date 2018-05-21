package basketbandit.core.tfl;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "$type",
        "category",
        "categoryDescription",
        "description",
        "additionalInfo",
        "created",
        "affectedRoutes",
        "affectedStops",
        "closureText"
})

public class Disruption {

    @JsonProperty("$type")
    private String $type;

    @JsonProperty("category")
    private String category;

    @JsonProperty("categoryDescription")
    private String categoryDescription;

    @JsonProperty("description")
    private String description;

    @JsonProperty("additionalInfo")
    private String additionalInfo;

    @JsonProperty("created")
    private String created;

    @JsonProperty("affectedRoutes")
    private List<Object> affectedRoutes = new ArrayList<Object>();

    @JsonProperty("affectedStops")
    private List<Object> affectedStops = new ArrayList<Object>();

    @JsonProperty("closureText")
    private String closureText;

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

    public Disruption with$type(String $type) {
        this.$type = $type;
        return this;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    public Disruption withCategory(String category) {
        this.category = category;
        return this;
    }

    @JsonProperty("categoryDescription")
    public String getCategoryDescription() {
        return categoryDescription;
    }

    @JsonProperty("categoryDescription")
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Disruption withCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
        return this;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    public Disruption withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("additionalInfo")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @JsonProperty("additionalInfo")
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Disruption withAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
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

    public Disruption withCreated(String created) {
        this.created = created;
        return this;
    }

    @JsonProperty("affectedRoutes")
    public List<Object> getAffectedRoutes() {
        return affectedRoutes;
    }

    @JsonProperty("affectedRoutes")
    public void setAffectedRoutes(List<Object> affectedRoutes) {
        this.affectedRoutes = affectedRoutes;
    }

    public Disruption withAffectedRoutes(List<Object> affectedRoutes) {
        this.affectedRoutes = affectedRoutes;
        return this;
    }

    @JsonProperty("affectedStops")
    public List<Object> getAffectedStops() {
        return affectedStops;
    }

    @JsonProperty("affectedStops")
    public void setAffectedStops(List<Object> affectedStops) {
        this.affectedStops = affectedStops;
    }

    public Disruption withAffectedStops(List<Object> affectedStops) {
        this.affectedStops = affectedStops;
        return this;
    }

    @JsonProperty("closureText")
    public String getClosureText() {
        return closureText;
    }

    @JsonProperty("closureText")
    public void setClosureText(String closureText) {
        this.closureText = closureText;
    }

    public Disruption withClosureText(String closureText) {
        this.closureText = closureText;
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

    public Disruption withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}