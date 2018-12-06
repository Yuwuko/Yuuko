package com.yuuko.core.modules.world.tesco;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "image",
        "superDepartment",
        "tpnb",
        "ContentsMeasureType",
        "name",
        "UnitOfSale",
        "AverageSellingUnitWeight",
        "description",
        "UnitQuantity",
        "id",
        "ContentsQuantity",
        "department",
        "price",
        "unitprice"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    @JsonProperty("image")
    private String image;
    @JsonProperty("superDepartment")
    private String superDepartment;
    @JsonProperty("tpnb")
    private Integer tpnb;
    @JsonProperty("ContentsMeasureType")
    private String contentsMeasureType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("UnitOfSale")
    private Integer unitOfSale;
    @JsonProperty("AverageSellingUnitWeight")
    private Double averageSellingUnitWeight;
    @JsonProperty("description")
    private List<String> description = null;
    @JsonProperty("UnitQuantity")
    private String unitQuantity;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("ContentsQuantity")
    private Double contentsQuantity;
    @JsonProperty("department")
    private String department;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("unitprice")
    private Double unitprice;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("superDepartment")
    public String getSuperDepartment() {
        return superDepartment;
    }

    @JsonProperty("tpnb")
    public Integer getTpnb() {
        return tpnb;
    }

    @JsonProperty("ContentsMeasureType")
    public String getContentsMeasureType() {
        return contentsMeasureType;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("UnitOfSale")
    public Integer getUnitOfSale() {
        return unitOfSale;
    }

    @JsonProperty("AverageSellingUnitWeight")
    public Double getAverageSellingUnitWeight() {
        return averageSellingUnitWeight;
    }

    @JsonProperty("description")
    public List<String> getDescription() {
        return description;
    }

    @JsonProperty("UnitQuantity")
    public String getUnitQuantity() {
        return unitQuantity;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ContentsQuantity")
    public Double getContentsQuantity() {
        return contentsQuantity;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("unitprice")
    public Double getUnitprice() {
        return unitprice;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

}