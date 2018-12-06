package com.yuuko.core.utils.json;

import com.yuuko.core.utils.Utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.net.URL;

public class JsonBuffer {

    /**
     * Takes an input URL, fetches any Json content from it and returns that as a string.
     * @param inputUrl String
     * @return result.toString();
     */
    public String getString(String inputUrl, String acceptHeader, String contentTypeHeader) {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {

            String accept = (acceptHeader.equals("default")) ? "application/json" : acceptHeader;
            String contentType = (contentTypeHeader.equals("default")) ? "application/json" : contentTypeHeader;
            HttpsURLConnection conn = (HttpsURLConnection) new URL(inputUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", accept);
            conn.setRequestProperty("Content-Type", contentType);

            if(conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }

            byte[] buffer = new byte[1024];
            int length;

            while((length = conn.getInputStream().read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString();

        } catch(Exception ex) {
            Utils.sendException(ex, "public String getString() [JsonBuffer]");
            return null;
        }
    }

}
