package com.basketbandit.core;

import com.google.api.services.youtube.model.SearchResult;
import pw.aru.api.nekos4j.Nekos4J;

import java.util.HashMap;
import java.util.List;

public class SystemVariables {

    /**
     * CommandSearch
     */
    public static HashMap<Long, List<SearchResult>> searchUsers = new HashMap<>();

    public static Nekos4J nekoApi = new Nekos4J.Builder().build();

}
