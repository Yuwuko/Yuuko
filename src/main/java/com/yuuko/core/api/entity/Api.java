package com.yuuko.core.api.entity;

public class Api {
    private String name;
    private String applicationId;
    private String apiKey;

    public Api() {

    }

    public Api(String name, String applicationId, String key) {
        this.name = name;
        this.applicationId = applicationId;
        this.apiKey = key;
    }

    public String getName() {
        return name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getKey() {
        return apiKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isAvailable() {
        return !name.equals("");
    }
}
