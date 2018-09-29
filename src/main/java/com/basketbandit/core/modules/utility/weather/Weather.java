package com.basketbandit.core.modules.utility.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "main",
        "description",
        "icon"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("main")
    private String main;
    @JsonProperty("description")
    private String description;
    @JsonProperty("icon")
    private String icon;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("main")
    public String getMain() {
        return main;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("icon")
    public String getIcon() {
        return icon;
    }

}
