package com.basketbandit.core.modules.media.kitsu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "createdAt",
        "updatedAt",
        "slug",
        "synopsis",
        "coverImageTopOffset",
        "titles",
        "canonicalTitle",
        "abbreviatedTitles",
        "averageRating",
        "ratingFrequencies",
        "userCount",
        "favoritesCount",
        "startDate",
        "endDate",
        "nextRelease",
        "popularityRank",
        "ratingRank",
        "ageRating",
        "ageRatingGuide",
        "subtype",
        "status",
        "tba",
        "posterImage",
        "coverImage",
        "episodeCount",
        "episodeLength",
        "totalLength",
        "youtubeVideoId",
        "showType",
        "nsfw"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes {

    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("synopsis")
    private String synopsis;
    @JsonProperty("coverImageTopOffset")
    private Integer coverImageTopOffset;
    @JsonProperty("titles")
    private Titles titles;
    @JsonProperty("canonicalTitle")
    private String canonicalTitle;
    @JsonProperty("abbreviatedTitles")
    private List<String> abbreviatedTitles = null;
    @JsonProperty("averageRating")
    private String averageRating;
    @JsonProperty("ratingFrequencies")
    private RatingFrequencies ratingFrequencies;
    @JsonProperty("userCount")
    private Integer userCount;
    @JsonProperty("favoritesCount")
    private Integer favoritesCount;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("nextRelease")
    private Object nextRelease;
    @JsonProperty("popularityRank")
    private Integer popularityRank;
    @JsonProperty("ratingRank")
    private Integer ratingRank;
    @JsonProperty("ageRating")
    private String ageRating;
    @JsonProperty("ageRatingGuide")
    private String ageRatingGuide;
    @JsonProperty("subtype")
    private String subtype;
    @JsonProperty("status")
    private String status;
    @JsonProperty("tba")
    private String tba;
    @JsonProperty("posterImage")
    private PosterImage posterImage;
    @JsonProperty("coverImage")
    private CoverImage coverImage;
    @JsonProperty("episodeCount")
    private Integer episodeCount;
    @JsonProperty("episodeLength")
    private Integer episodeLength;
    @JsonProperty("totalLength")
    private Integer totalLength;
    @JsonProperty("youtubeVideoId")
    private String youtubeVideoId;
    @JsonProperty("showType")
    private String showType;
    @JsonProperty("nsfw")
    private Boolean nsfw;

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("synopsis")
    public String getSynopsis() {
        return synopsis;
    }

    @JsonProperty("coverImageTopOffset")
    public Integer getCoverImageTopOffset() {
        return coverImageTopOffset;
    }

    @JsonProperty("titles")
    public Titles getTitles() {
        return titles;
    }

    @JsonProperty("canonicalTitle")
    public String getCanonicalTitle() {
        return canonicalTitle;
    }

    @JsonProperty("abbreviatedTitles")
    public List<String> getAbbreviatedTitles() {
        return abbreviatedTitles;
    }

    @JsonProperty("averageRating")
    public String getAverageRating() {
        return averageRating;
    }

    @JsonProperty("ratingFrequencies")
    public RatingFrequencies getRatingFrequencies() {
        return ratingFrequencies;
    }

    @JsonProperty("userCount")
    public Integer getUserCount() {
        return userCount;
    }

    @JsonProperty("favoritesCount")
    public Integer getFavoritesCount() {
        return favoritesCount;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("nextRelease")
    public Object getNextRelease() {
        return nextRelease;
    }

    @JsonProperty("popularityRank")
    public Integer getPopularityRank() {
        return popularityRank;
    }

    @JsonProperty("ratingRank")
    public Integer getRatingRank() {
        return ratingRank;
    }

    @JsonProperty("ageRating")
    public String getAgeRating() {
        return ageRating;
    }

    @JsonProperty("ageRatingGuide")
    public String getAgeRatingGuide() {
        return ageRatingGuide;
    }

    @JsonProperty("subtype")
    public String getSubtype() {
        return subtype;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("tba")
    public String getTba() {
        return tba;
    }

    @JsonProperty("posterImage")
    public PosterImage getPosterImage() {
        return posterImage;
    }

    @JsonProperty("coverImage")
    public CoverImage getCoverImage() {
        return coverImage;
    }

    @JsonProperty("episodeCount")
    public Integer getEpisodeCount() {
        return episodeCount;
    }

    @JsonProperty("episodeLength")
    public Integer getEpisodeLength() {
        return episodeLength;
    }

    @JsonProperty("totalLength")
    public Integer getTotalLength() {
        return totalLength;
    }

    @JsonProperty("youtubeVideoId")
    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    @JsonProperty("showType")
    public String getShowType() {
        return showType;
    }

    @JsonProperty("nsfw")
    public Boolean getNsfw() {
        return nsfw;
    }

}
