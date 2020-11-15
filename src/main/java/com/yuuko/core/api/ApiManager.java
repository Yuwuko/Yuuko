package com.yuuko.core.api;

import com.yuuko.core.api.entity.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiManager {
    private static final Logger log = LoggerFactory.getLogger(ApiManager.class);
    private final HashMap<String, Api> API = new HashMap<>();

    public ApiManager() {
        Yaml yaml = new Yaml();
        for(File file : new File("./config/api/").listFiles()) {
            try(InputStream inputStream = new FileInputStream(file)) {
                Api api = yaml.load(inputStream);
                API.put(api.getName(), api);
            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", getClass().getSimpleName(), e.getMessage(), e);
            }
        }
    }

    public Api getApi(String api) {
        return API.getOrDefault(api, new Api("", "", ""));
    }

    public boolean containsKey(String key) {
        return API.containsKey(key);
    }

    public List<String> getNames() {
        return new ArrayList<>(API.keySet());
    }

    public int size() {
        return API.size();
    }
}
