package com.basketbandit.core.modules.media.kitsu;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class KitsuContainer {

    @JsonProperty("data")
    private List<Datum> data = null;

    @JsonProperty("data")
    public List<Datum> getData() {
        return data;
    }

}
