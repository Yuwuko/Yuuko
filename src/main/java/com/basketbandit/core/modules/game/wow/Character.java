package com.basketbandit.core.modules.game.wow;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "lastModified",
        "name",
        "realm",
        "battlegroup",
        "class",
        "race",
        "gender",
        "level",
        "achievementPoints",
        "thumbnail",
        "calcClass",
        "faction",
        "guild",
        "titles",
        "totalHonorableKills"
})

public class Character {

    @JsonProperty("lastModified")
    private Integer lastModified;

    @JsonProperty("name")
    private String name;

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("battlegroup")
    private String battlegroup;

    @JsonProperty("class")
    private Integer _class;

    @JsonProperty("race")
    private Integer race;

    @JsonProperty("gender")
    private Integer gender;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("achievementPoints")
    private Integer achievementPoints;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("calcClass")
    private String calcClass;

    @JsonProperty("faction")
    private Integer faction;

    @JsonProperty("guild")
    private Guild guild;

    @JsonProperty("titles")
    private List<Title> titles = null;

    @JsonProperty("totalHonorableKills")
    private Integer totalHonorableKills;

    @JsonProperty("lastModified")
    public Integer getLastModified() {
        return lastModified;
    }

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

    @JsonProperty("class")
    public Integer get_class() {
        return _class;
    }

    @JsonProperty("race")
    public Integer getRace() {
        return race;
    }

    @JsonProperty("gender")
    public Integer getGender() {
        return gender;
    }

    @JsonProperty("level")
    public Integer getLevel() {
        return level;
    }

    @JsonProperty("achievementPoints")
    public Integer getAchievementPoints() {
        return achievementPoints;
    }

    @JsonProperty("thumbnail")
    public String getThumbnail() {
        return thumbnail;
    }

    @JsonProperty("calcClass")
    public String getCalcClass() {
        return calcClass;
    }

    @JsonProperty("faction")
    public Integer getFaction() {
        return faction;
    }

    @JsonProperty("guild")
    public Guild getGuild() {
        return guild;
    }

    @JsonProperty("titles")
    public List<Title> getTitles() {
        return titles;
    }

    @JsonProperty("totalHonorableKills")
    public Integer getTotalHonorableKills() {
        return totalHonorableKills;
    }

}