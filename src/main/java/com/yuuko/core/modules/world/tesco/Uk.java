package com.yuuko.core.modules.world.tesco;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ghs"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Uk {

    @JsonProperty("ghs")
    private Ghs ghs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("ghs")
    public Ghs getGhs() {
        return ghs;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}