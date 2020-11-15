package com.yuuko.core.api.entity;

public class Api {
    private final String name;
    private final String applicationId;
    private final String key;

    public Api(String name, String applicationId, String key) {
        this.name = name;
        this.applicationId = applicationId;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getKey() {
        return key;
    }

    public boolean isAvailable() {
        return !name.equals("");
    }
}
