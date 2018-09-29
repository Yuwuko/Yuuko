package com.basketbandit.core.modules.media.kitsu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "en",
        "en_jp",
        "en_us",
        "ja_jp"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Titles {

    @JsonProperty("en")
    private String en;
    @JsonProperty("en_jp")
    private String enJp;
    @JsonProperty("en_us")
    private String enUs;
    @JsonProperty("ja_jp")
    private String jaJp;

    @JsonProperty("en")
    public String getEn() {
        return en;
    }

    @JsonProperty("en_jp")
    public String getEnJp() {
        return enJp;
    }

    @JsonProperty("en_us")
    public String getEnUs() {
        return enUs;
    }

    @JsonProperty("ja_jp")
    public String getJaJp() {
        return jaJp;
    }

}