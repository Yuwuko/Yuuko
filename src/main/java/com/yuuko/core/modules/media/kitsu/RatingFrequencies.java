package com.yuuko.core.modules.media.kitsu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20"
})

@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingFrequencies {

    @JsonProperty("2")
    private String _2;
    @JsonProperty("3")
    private String _3;
    @JsonProperty("4")
    private String _4;
    @JsonProperty("5")
    private String _5;
    @JsonProperty("6")
    private String _6;
    @JsonProperty("7")
    private String _7;
    @JsonProperty("8")
    private String _8;
    @JsonProperty("9")
    private String _9;
    @JsonProperty("10")
    private String _10;
    @JsonProperty("11")
    private String _11;
    @JsonProperty("12")
    private String _12;
    @JsonProperty("13")
    private String _13;
    @JsonProperty("14")
    private String _14;
    @JsonProperty("15")
    private String _15;
    @JsonProperty("16")
    private String _16;
    @JsonProperty("17")
    private String _17;
    @JsonProperty("18")
    private String _18;
    @JsonProperty("19")
    private String _19;
    @JsonProperty("20")
    private String _20;

    @JsonProperty("2")
    public String get2() {
        return _2;
    }

    @JsonProperty("3")
    public String get3() {
        return _3;
    }

    @JsonProperty("4")
    public String get4() {
        return _4;
    }

    @JsonProperty("5")
    public String get5() {
        return _5;
    }

    @JsonProperty("6")
    public String get6() {
        return _6;
    }

    @JsonProperty("7")
    public String get7() {
        return _7;
    }

    @JsonProperty("8")
    public String get8() {
        return _8;
    }

    @JsonProperty("9")
    public String get9() {
        return _9;
    }

    @JsonProperty("10")
    public String get10() {
        return _10;
    }

    @JsonProperty("11")
    public String get11() {
        return _11;
    }

    @JsonProperty("12")
    public String get12() {
        return _12;
    }

    @JsonProperty("13")
    public String get13() {
        return _13;
    }

    @JsonProperty("14")
    public String get14() {
        return _14;
    }

    @JsonProperty("15")
    public String get15() {
        return _15;
    }

    @JsonProperty("16")
    public String get16() {
        return _16;
    }

    @JsonProperty("17")
    public String get17() {
        return _17;
    }

    @JsonProperty("18")
    public String get18() {
        return _18;
    }

    @JsonProperty("19")
    public String get19() {
        return _19;
    }

    @JsonProperty("20")
    public String get20() {
        return _20;
    }

}
