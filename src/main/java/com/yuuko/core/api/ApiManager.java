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
    private HashMap<String, Api> API;

    public ApiManager() throws IOException {
        File folder = new File("./config/api/");
        File[] keyFiles = folder.listFiles();

        if(keyFiles != null) {
            API = new HashMap<>(keyFiles.length);
            for(File key : keyFiles) {
                BufferedReader c = new BufferedReader(new FileReader(key));
                API.put(key.getName(), new Api(key.getName(), c.readLine(), c.readLine()));
                c.close();
            }
        }
    }

    public Api getApi(String key) {
        return API.get(key);
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
