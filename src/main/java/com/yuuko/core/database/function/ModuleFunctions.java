package com.yuuko.core.database.function;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.connection.YuukoDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class ModuleFunctions {

    private static final Logger log = LoggerFactory.getLogger(ModuleFunctions.class);

    /**
     * Retrieves all of the module settings for a guild and returns them in an arrayList of an arrayList.
     *
     * @param guild the guild id.
     * @return ArrayList<ArrayList<String>>
     */
    public static ArrayList<ArrayList<String>> getModuleSettings(String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `module_settings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
            ArrayList<String> enabled = new ArrayList<>();
            ArrayList<String> disabled = new ArrayList<>();

            if(rs.next()) {
                for(int i = 2; i < Configuration.MODULES.size(); i++) {
                    if(rs.getBoolean(i)) {
                        enabled.add(meta.getColumnName(i));
                    } else {
                        disabled.add(meta.getColumnName(i));
                    }
                }
            } else {
                return new ArrayList<>();
            }

            arrayLists.add(enabled);
            arrayLists.add(disabled);

            return arrayLists;
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Checks to see if a module is active before parsing a command.
     *
     * @param guild the guild id to check against.
     * @param module the name of the module.
     * @return (boolean) if the module is active or not.
     */
    public static boolean isEnabled(String guild, String module) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + module + "` FROM `module_settings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next() && resultSet.getBoolean(1);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Toggles a module for a guild, returns the new value.
     *
     * @param module the module to toggle.
     * @param guild the guild in which the module is to be toggled.
     * @return boolean.
     */
    public static boolean toggleModule(String guild, String module) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `module_settings` SET `" + module + "` = NOT `" + module + "` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            stmt.execute();

            return isEnabled(guild, module);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}
