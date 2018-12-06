package com.yuuko.core.modules.world.tesco;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "products"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ghs {

    @JsonProperty("products")
    private Products products;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("products")
    public Products getProducts() {
        return products;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}
