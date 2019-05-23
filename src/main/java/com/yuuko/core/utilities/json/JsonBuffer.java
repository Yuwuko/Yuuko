package com.yuuko.core.utilities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonBuffer {
    private static final Logger log = LoggerFactory.getLogger(JsonBuffer.class);
    private static final OkHttpClient client = new OkHttpClient();
    private String jsonOutput;

    public JsonBuffer(String url, String acceptDirective, String contentTypeDirective, RequestProperty... extraProperties) {
        try {
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .addHeader("Accept", (acceptDirective.equals("default")) ? "application/json" : acceptDirective)
                    .addHeader("Content-Type", (contentTypeDirective.equals("default")) ? "application/json" : contentTypeDirective);
            if(extraProperties != null && extraProperties.length > 0) {
                for(RequestProperty property : extraProperties) {
                    builder.addHeader(property.getHeader(), property.getDirective());
                }
            }
            Response response = client.newCall(builder.build()).execute();

            if(response.code() != 200) {
                response.close();
                return;
            }

            if(response.body() != null) {
                jsonOutput = response.body().string();
            }

            response.close();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves the json output as a string, does nothing else to it.
     *
     * @return String
     */
    public String getAsString() {
        return jsonOutput;
    }

    /**
     * Retrieves the json output as a JsonObject which can be handled and manipulated with the Google Gson package.
     *
     * @return JsonObject
     * @throws IllegalStateException IllegalStateException
     */
    public JsonObject getAsJsonObject() throws IllegalStateException {
        try {
            return (jsonOutput == null) ? null : new JsonParser().parse(jsonOutput).getAsJsonObject();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Retrieves the json output as a JsonArray which can be handled and manipulated with the Google Gson package.
     *
     * @return JsonArray
     * @throws IllegalStateException IllegalStateException
     */
    public JsonArray getAsJsonArray() throws IllegalStateException {
        try {
            return (jsonOutput == null) ? null : new JsonParser().parse(jsonOutput).getAsJsonArray();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
            return null;
        }
    }

}
