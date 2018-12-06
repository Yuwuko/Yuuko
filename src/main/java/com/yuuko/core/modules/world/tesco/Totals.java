package com.yuuko.core.modules.world.tesco;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "all",
        "new",
        "offer"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Totals {

    @JsonProperty("all")
    private Integer all;
    @JsonProperty("new")
    private Integer _new;
    @JsonProperty("offer")
    private Integer offer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("all")
    public Integer getAll() {
        return all;
    }

    @JsonProperty("new")
    public Integer getNew() {
        return _new;
    }

    @JsonProperty("offer")
    public Integer getOffer() {
        return offer;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}