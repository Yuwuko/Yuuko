package com.basketbandit.core.utils.json;

import com.basketbandit.core.utils.Utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.net.URL;

public class JsonBuffer {

    /**
     * Takes an input URL, fetches any Json content from it and returns that as a string.
     * @param inputUrl String
     * @return result.toString();
     */
    public String getString(String inputUrl) {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            HttpsURLConnection conn = (HttpsURLConnection) new URL(inputUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

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
            Utils.sendException(ex.getMessage());
            return null;
        }
    }

}
