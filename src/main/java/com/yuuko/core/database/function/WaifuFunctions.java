package com.yuuko.core.database.function;

import com.yuuko.core.database.connection.WaifuDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class WaifuFunctions {

    private static final Logger log = LoggerFactory.getLogger(BindFunctions.class);

    /**
     * Binds a particular module to a channel.
     *
     * @param userId the id of the user.
     * @return boolean
     */
    public static HashMap<String, String> getWaifu(String userId) {
        final HashMap<String, String> waifu = new HashMap<>();

        try(Connection conn = WaifuDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `UserWaifus` WHERE `userid` = ?");
            PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM `Waifus` WHERE `waifuId` = ?")) {

            stmt.setString(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                stmt2.setString(1, resultSet.getString("waifuId"));
                ResultSet waifuSet = stmt2.executeQuery();

                if(waifuSet.next()) {
                    waifu.put("image", waifuSet.getString("waifuImage"));
                    waifu.put("name", waifuSet.getString("waifuName"));
                    waifu.put("age", waifuSet.getString("waifuAge"));
                    waifu.put("height", waifuSet.getString("waifuHeight"));
                    return waifu;
                }
            }

            return waifu;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return waifu;
        }
    }
}
