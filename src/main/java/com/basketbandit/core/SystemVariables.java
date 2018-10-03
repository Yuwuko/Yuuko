package com.basketbandit.core;

import com.google.api.services.youtube.model.SearchResult;

import java.util.HashMap;
import java.util.List;

public class SystemVariables {

    /**
     * CommandSearch
     */
    public static HashMap<Long, List<SearchResult>> searchUsers = new HashMap<>();

}
