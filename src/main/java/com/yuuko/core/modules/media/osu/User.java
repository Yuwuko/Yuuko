package com.yuuko.core.modules.media.osu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_id",
        "username",
        "count300",
        "count100",
        "count50",
        "playcount",
        "ranked_score",
        "total_score",
        "pp_rank",
        "profile",
        "pp_raw",
        "accuracy",
        "count_rank_ss",
        "count_rank_ssh",
        "count_rank_s",
        "count_rank_sh",
        "count_rank_a",
        "country",
        "total_seconds_played",
        "pp_country_rank",
        "events"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("count300")
    private String count300;
    @JsonProperty("count100")
    private String count100;
    @JsonProperty("count50")
    private String count50;
    @JsonProperty("playcount")
    private String playcount;
    @JsonProperty("ranked_score")
    private String rankedScore;
    @JsonProperty("total_score")
    private String totalScore;
    @JsonProperty("pp_rank")
    private String ppRank;
    @JsonProperty("profile")
    private String level;
    @JsonProperty("pp_raw")
    private String ppRaw;
    @JsonProperty("accuracy")
    private String accuracy;
    @JsonProperty("count_rank_ss")
    private String countRankSs;
    @JsonProperty("count_rank_ssh")
    private String countRankSsh;
    @JsonProperty("count_rank_s")
    private String countRankS;
    @JsonProperty("count_rank_sh")
    private String countRankSh;
    @JsonProperty("count_rank_a")
    private String countRankA;
    @JsonProperty("country")
    private String country;
    @JsonProperty("total_seconds_played")
    private String totalSecondsPlayed;
    @JsonProperty("pp_country_rank")
    private String ppCountryRank;
    @JsonProperty("events")
    private List<Event> events = null;

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("count300")
    public String getCount300() {
        return count300;
    }

    @JsonProperty("count100")
    public String getCount100() {
        return count100;
    }

    @JsonProperty("count50")
    public String getCount50() {
        return count50;
    }

    @JsonProperty("playcount")
    public String getPlaycount() {
        return playcount;
    }

    @JsonProperty("ranked_score")
    public String getRankedScore() {
        return rankedScore;
    }

    @JsonProperty("total_score")
    public String getTotalScore() {
        return totalScore;
    }

    @JsonProperty("pp_rank")
    public String getPpRank() {
        return ppRank;
    }

    @JsonProperty("profile")
    public String getLevel() {
        return level;
    }

    @JsonProperty("pp_raw")
    public String getPpRaw() {
        return ppRaw;
    }

    @JsonProperty("accuracy")
    public String getAccuracy() {
        return accuracy;
    }

    @JsonProperty("count_rank_ss")
    public String getCountRankSs() {
        return countRankSs;
    }

    @JsonProperty("count_rank_ssh")
    public String getCountRankSsh() {
        return countRankSsh;
    }

    @JsonProperty("count_rank_s")
    public String getCountRankS() {
        return countRankS;
    }

    @JsonProperty("count_rank_sh")
    public String getCountRankSh() {
        return countRankSh;
    }

    @JsonProperty("count_rank_a")
    public String getCountRankA() {
        return countRankA;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("total_seconds_played")
    public String getTotalSecondsPlayed() {
        return totalSecondsPlayed;
    }

    @JsonProperty("pp_country_rank")
    public String getPpCountryRank() {
        return ppCountryRank;
    }

    @JsonProperty("events")
    public List<Event> getEvents() {
        return events;
    }
}