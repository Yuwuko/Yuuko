package com.yuuko.core;

public class ApplicationProgrammingInterface {

    private final String name;
    private final String applicationId;
    private final String key;

    ApplicationProgrammingInterface(String name, String applicationId, String key) {
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
}
