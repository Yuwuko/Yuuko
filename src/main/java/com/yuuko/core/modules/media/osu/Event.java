package com.yuuko.core.modules.media.osu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "display_html",
        "beatmap_id",
        "beatmapset_id",
        "date",
        "epicfactor"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    @JsonProperty("display_html")
    private String displayHtml;
    @JsonProperty("beatmap_id")
    private Object beatmapId;
    @JsonProperty("beatmapset_id")
    private Object beatmapsetId;
    @JsonProperty("date")
    private String date;
    @JsonProperty("epicfactor")
    private String epicfactor;

    @JsonProperty("display_html")
    public String getDisplayHtml() {
        return displayHtml;
    }

    @JsonProperty("beatmap_id")
    public Object getBeatmapId() {
        return beatmapId;
    }

    @JsonProperty("beatmapset_id")
    public Object getBeatmapsetId() {
        return beatmapsetId;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("epicfactor")
    public String getEpicfactor() {
        return epicfactor;
    }

}