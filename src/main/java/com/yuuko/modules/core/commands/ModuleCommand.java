package com.yuuko.modules.core.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.connection.DatabaseConnection;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleCommand extends Command {

    public ModuleCommand() {
        super("module", 0, -1L, Arrays.asList("-module <module>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(context.hasParameters()) {
            String module = context.getParameters().split("\\s+", 2)[0].toLowerCase();
            String guild = context.getGuild().getId();

            if(!Yuuko.MODULES.containsKey(module)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "invalid_module").formatted(module));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            // Prevents locked modules from being disabled (would throw exception anyway)
            if(Yuuko.LOCKED_MODULES.contains(module)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("core_title"))
                        .setDescription(context.i18n( "core_desc").formatted(Yuuko.LOCKED_MODULES.toString()));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            if(DatabaseInterface.toggleModule(guild, module)) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle(context.i18n("enabled").formatted(module));
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle(context.i18n("disabled").formatted(module));
                MessageDispatcher.reply(context, embed.build());
            }
        } else {
            ArrayList<ArrayList<String>> settings = DatabaseInterface.getModuleSettings(context.getGuild().getId());
            EmbedBuilder commandModules = new EmbedBuilder()
                    .setTitle(context.i18n( "title"))
                    .setDescription(context.i18n( "desc").formatted(context.getPrefix()))
                    .addField(context.i18n("enabled_modules").formatted(settings.get(0).size()), settings.get(0).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .addField(context.i18n("disabled_modules").formatted(settings.get(1).size()), settings.get(1).toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), true)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, commandModules.build());
        }
    }

    /**
     * Inner-class container for all database-related functions.
     */
    public static class DatabaseInterface {
        /**
         * Retrieves all of the module settings for a guild and returns them in an arrayList of an arrayList.
         *
         * @param guild the guild id.
         * @return ArrayList<ArrayList<String>>
         */
        public static ArrayList<ArrayList<String>> getModuleSettings(String guild) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_module_settings` WHERE `guildId` = ?")) {

                stmt.setString(1, guild);
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();

                ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
                ArrayList<String> enabled = new ArrayList<>();
                ArrayList<String> disabled = new ArrayList<>();

                if(rs.next()) {
                    for(int i = 2; i < Yuuko.MODULES.size() - 1; i++) {
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
            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ModuleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
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
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT `" + module + "` FROM `guilds_module_settings` WHERE `guildId` = ?")) {

                stmt.setString(1, guild);
                ResultSet resultSet = stmt.executeQuery();

                return resultSet.next() && resultSet.getBoolean(1);

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ModuleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
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
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_module_settings` SET `" + module + "` = NOT `" + module + "` WHERE `guildId` = ?")) {

                stmt.setString(1, guild);
                stmt.execute();

                return isEnabled(guild, module);

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ModuleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return false;
            }
        }
    }

}
