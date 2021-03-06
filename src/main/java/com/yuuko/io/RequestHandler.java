package com.yuuko.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuuko.io.entity.RequestProperty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final OkHttpClient client = new OkHttpClient();
    private String content;

    /**
     * RequestHandler method which takes a url and optional arguments as request properties.
     * @param url String
     * @param requestProperties {@link RequestProperty}
     */
    public RequestHandler(String url, RequestProperty... requestProperties) {
        Request.Builder builder = new Request.Builder().url(url);

        // if request properties is length 0, ask for json by default
        if(requestProperties.length == 0) {
            requestProperties = new RequestProperty[]{
                    new RequestProperty("Accept", "application/json"),
                    new RequestProperty("Content-Type","application/json")
            };
        }

        for(RequestProperty property : requestProperties) {
            builder.addHeader(property.header(), property.directive());
        }

        try(Response response = client.newCall(builder.build()).execute()) {
            if(response.code() == 200) {
                this.content = response.body().string();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Retrieves the content as a string, does nothing else to it.
     * @return String
     */
    public String getString() {
        return content;
    }

    /**
     * Retrieves the content as a JsonObject which can be handled and manipulated with the Google Gson package.
     * @return {@link JsonObject}
     * @throws IllegalStateException IllegalStateException
     */
    public JsonObject getJsonObject() throws IllegalStateException {
        try {
            return (content == null) ? null : JsonParser.parseString(content).getAsJsonObject();
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieves the content as a JsonArray which can be handled and manipulated with the Google Gson package.
     * @return {@link JsonArray}
     * @throws IllegalStateException IllegalStateException
     */
    public JsonArray getJsonArray() throws IllegalStateException {
        try {
            return (content == null) ? null : JsonParser.parseString(content).getAsJsonArray();
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Retrieves the content as a Jsoup Document.
     * @return {@link Document}
     */
    public Document getDocument() {
        try {
            return (content == null) ? null : Jsoup.parse(content);
        } catch(Exception e) {
            log.error("There was a problem parsing that content, class: {}, message: {}", this, e.getMessage(), e);
            return null;
        }
    }

}
