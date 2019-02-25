package com.yuuko.core.utilities.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yuuko.core.utilities.MessageHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.net.URL;

public class JsonBuffer {

    private String jsonOutput;

    public JsonBuffer(String inputUrl, String acceptHeader, String contentTypeHeader, String extraProperty, String extraHeader) {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            String accept = (acceptHeader.equals("default")) ? "application/json" : acceptHeader;
            String contentType = (contentTypeHeader.equals("default")) ? "application/json" : contentTypeHeader;
            HttpsURLConnection conn = (HttpsURLConnection) new URL(inputUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", accept);
            conn.setRequestProperty("Content-Type", contentType);

            if(extraProperty != null && extraHeader != null) {
                conn.setRequestProperty(extraProperty, extraHeader);
            }

            if(conn.getResponseCode() != 200) {
                return;
            }

            byte[] buffer = new byte[1024];
            int length;

            while((length = conn.getInputStream().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            jsonOutput = result.toString();

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "public String getAsString() [JsonBuffer]");
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
            MessageHandler.sendException(ex, "getAsJsonObject()");
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
            MessageHandler.sendException(ex, "getAsJsonArray()");
            return null;
        }
    }

}
