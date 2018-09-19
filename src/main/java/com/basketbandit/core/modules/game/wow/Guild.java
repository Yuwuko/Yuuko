package com.basketbandit.core.modules.game.wow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "realm",
        "battlegroup",
        "members",
        "achievementPoints",
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Guild {

    @JsonProperty("name")
    private String name;

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("battlegroup")
    private String battlegroup;

    @JsonProperty("members")
    private Integer members;

    @JsonProperty("achievementPoints")
    private Integer achievementPoints;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("realm")
    public String getRealm() {
        return realm;
    }

    @JsonProperty("battlegroup")
    public String getBattlegroup() {
        return battlegroup;
    }

    @JsonProperty("members")
    public Integer getMembers() {
        return members;
    }

    @JsonProperty("achievementPoints")
    public Integer getAchievementPoints() {
        return achievementPoints;
    }
}
