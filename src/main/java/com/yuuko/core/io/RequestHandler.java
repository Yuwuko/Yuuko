package com.yuuko.core.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuuko.core.io.entity.RequestProperty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    // new OkHttpClient.Builder().addInterceptor(BrotliInterceptor.INSTANCE) -> figure out why issue (later)
    private static final OkHttpClient client = new OkHttpClient();
    private String content;

    private static final String JSON = "application/json";

    /**
     * RequestHandler method which takes a url and optional arguments as request properties.
     * Providing no request property parameters will cause defaults to be used which are
     * Accept: application/json && Content-Type: application/json
     *
     * @param url String: the url used for the connection
     * @param requestProperties RequestProperty: the optional request properties with default application/json when none are given.
     */
    public RequestHandler(String url, RequestProperty... requestProperties) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(url);

            if(requestProperties != null && requestProperties.length > 0) {
                for(RequestProperty property : requestProperties) {
                    builder.addHeader(property.getHeader(), property.getDirective());
                }
            } else {
                   builder.addHeader("Accept", JSON)
                   .addHeader("Content-Type", JSON);
            }

            Response response = client.newCall(builder.build()).execute();

            if(response.code() != 200) {
                response.close();
                return;
            }

            if(response.body() != null) {
                content = response.body().string();
            }

            response.close();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves the content as a string, does nothing else to it.
     *
     * @return String
     */
    public String getString() {
        return content;
    }

    /**
     * Retrieves the content as a JsonObject which can be handled and manipulated with the Google Gson package.
     *
     * @return {@link JsonObject}
     * @throws IllegalStateException IllegalStateException
     */
    public JsonObject getJsonObject() throws IllegalStateException {
        try {
            return (content == null) ? null : new JsonParser().parse(content).getAsJsonObject();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Retrieves the content as a JsonArray which can be handled and manipulated with the Google Gson package.
     *
     * @return {@link JsonArray}
     * @throws IllegalStateException IllegalStateException
     */
    public JsonArray getJsonArray() throws IllegalStateException {
        try {
            return (content == null) ? null : new JsonParser().parse(content).getAsJsonArray();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Retrieves the content as a Jsoup Document.
     *
     * @return {@link Document}
     */
    public Document getDocument() {
        try {
            return (content == null) ? null : Jsoup.parse(content);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
            return null;
        }
    }

}
