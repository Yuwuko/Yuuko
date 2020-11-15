package com.yuuko.core.api;

import com.yuuko.core.api.entity.Api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApiManager {
    private final HashMap<String, Api> API = new HashMap<>();

    public ApiManager() throws IOException {
        for(File key : new File("./config/api/").listFiles()) {
            BufferedReader c = new BufferedReader(new FileReader(key));
            API.put(key.getName(), new Api(key.getName(), c.readLine(), c.readLine()));
            c.close();
        }
    }

    public Api getApi(String key) {
        return API.getOrDefault(key, new Api("", "", ""));
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
