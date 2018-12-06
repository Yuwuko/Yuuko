package com.yuuko.core.modules.media.kitsu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tiny",
        "small",
        "large",
        "original"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoverImage {

    @JsonProperty("tiny")
    private String tiny;
    @JsonProperty("small")
    private String small;
    @JsonProperty("large")
    private String large;
    @JsonProperty("original")
    private String original;

    @JsonProperty("tiny")
    public String getTiny() {
        return tiny;
    }

    @JsonProperty("small")
    public String getSmall() {
        return small;
    }

    @JsonProperty("large")
    public String getLarge() {
        return large;
    }

    @JsonProperty("original")
    public String getOriginal() {
        return original;
    }

}
