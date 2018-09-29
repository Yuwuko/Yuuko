package com.basketbandit.core.modules.utility.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lon",
        "lat"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coord {

    @JsonProperty("lon")
    private Double lon;
    @JsonProperty("lat")
    private Double lat;

    @JsonProperty("lon")
    public Double getLon() {
        return lon;
    }

    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

}
