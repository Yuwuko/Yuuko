package com.yuuko.core.modules.world.tesco;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "input_query",
        "output_query",
        "filters",
        "queryPhase",
        "totals",
        "config",
        "results",
        "suggestions"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Products {

    @JsonProperty("input_query")
    private String inputQuery;
    @JsonProperty("output_query")
    private String outputQuery;
    @JsonProperty("filters")
    private Filters filters;
    @JsonProperty("queryPhase")
    private String queryPhase;
    @JsonProperty("totals")
    private Totals totals;
    @JsonProperty("config")
    private String config;
    @JsonProperty("results")
    private List<Result> results = null;
    @JsonProperty("suggestions")
    private List<Object> suggestions = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("input_query")
    public String getInputQuery() {
        return inputQuery;
    }

    @JsonProperty("output_query")
    public String getOutputQuery() {
        return outputQuery;
    }

    @JsonProperty("filters")
    public Filters getFilters() {
        return filters;
    }

    @JsonProperty("queryPhase")
    public String getQueryPhase() {
        return queryPhase;
    }

    @JsonProperty("totals")
    public Totals getTotals() {
        return totals;
    }

    @JsonProperty("config")
    public String getConfig() {
        return config;
    }

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("suggestions")
    public List<Object> getSuggestions() {
        return suggestions;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
