package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", Config.MODULES.get("core"), 0, -1L, Arrays.asList("-bind <module>", "-bind <module> #channel..."), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(e.hasParameters()) {
            String[] params = e.getParameters().toLowerCase().split("\\s+", 2);

            if(!params[0].equals("*") && !Config.MODULES.containsKey(params[0])) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + params[0] + "** isn't a valid module. A list of valid module can be found by using the **" + Utilities.getServerPrefix(e.getGuild()) + "help** command.");
                MessageDispatcher.reply(e, embed.build());
                return;
            }

            String module = params[0].equals("*") ? "*" : Config.MODULES.get(params[0]).getName();

            if(params.length > 1) {
                List<TextChannel> channels = e.getMessage().getMentionedChannels();

                if(channels.size() < 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Expected at least **1** \\#tagged channel to bind, found **0**.");
                    MessageDispatcher.reply(e, embed.build());
                    return;
                }

                StringBuilder boundChannels = new StringBuilder();
                for(TextChannel channel : channels) {
                    DatabaseInterface.toggleBind(e.getGuild().getId(), channel.getId(), module);
                    boundChannels.append(channel.getName()).append(", ");
                }
                TextUtilities.removeLast(boundChannels, ", ");

                EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully toggled **" + module + "** on **" + boundChannels.toString() + "**.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                final int res = DatabaseInterface.toggleBind(e.getGuild().getId(), e.getChannel().getId(), module);

                if(res == 0) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully bound **" + module + "** to **" + e.getChannel().getName() + "**.");
                    MessageDispatcher.reply(e, embed.build());
                } else if(res == 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully removed binding of **" + module + "** from **" + e.getChannel().getName() + "**.");
                    MessageDispatcher.reply(e, embed.build());
                }
            }
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Bound Modules")
                .setDescription(DatabaseInterface.getGuildBinds(e.getGuild(), "\n"));
        MessageDispatcher.reply(e, embed.build());
    }

    public static class DatabaseInterface {
        /**
         * Binds a particular module to a channel.
         *
         * @param guildId the idLong of the guild.
         * @param channel the idLong of the channel.
         * @param module the name of the module.
         * @return boolean
         */
        public static int toggleBind(String guildId, String channel, String module) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_module_binds` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?");
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `guilds_module_binds`(`guildId`, `channelId`, `moduleName`) VALUES (?,?,?)")) {

                stmt.setString(1, guildId);
                stmt.setString(2, channel);
                stmt.setString(3, module);
                ResultSet resultSet = stmt.executeQuery();

                if(!resultSet.next()) {
                    stmt2.setString(1, guildId);
                    stmt2.setString(2, channel);
                    stmt2.setString(3, module);
                    if(!stmt2.execute()) {
                        return 0;
                    }
                }

                return (clearBind(guildId, channel, module)) ? 1 : -1;

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return -1;
            }
        }

        /**
         * Returns a formatted string of all of the selected guild's binds.
         *
         * @param guild a guild object.
         * @param delimiter the delimiter used in the returned string.
         * @return String
         */
        public static String getGuildBinds(Guild guild, String delimiter) {
            try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `guilds_module_binds` WHERE `guildId` = ? ORDER BY `channelId`")) {

                stmt.setString(1, guild.getId());
                ResultSet rs = stmt.executeQuery();

                StringBuilder string = new StringBuilder();
                while(rs.next()) {
                    string.append(rs.getString(3)).append(" : ").append(guild.getTextChannelCache().getElementById(rs.getString(2)).getAsMention()).append(" (").append(guild.getTextChannelCache().getElementById(rs.getString(2)).getId()).append(")").append(delimiter);
                }

                if(string.length() > 0) {
                    TextUtilities.removeLast(string, delimiter);
                } else {
                    string.append("None");
                }

                return string.toString();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return null;
            }
        }

        /**
         * Returns a formatted string of all of the guild's binds, which match a given module.
         *
         * @param guild Guild
         * @param module String
         * @param delimiter String
         * @return String
         */
        public static String getBindsByModule(Guild guild, String module, String delimiter) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_module_binds` WHERE (`guildId` = ? AND `moduleName` = ?) OR (`guildId` = ? AND `moduleName` = '*')")) {

                stmt.setString(1, guild.getId());
                stmt.setString(2, module);
                stmt.setString(3, guild.getId());

                ResultSet rs = stmt.executeQuery();

                StringBuilder string = new StringBuilder();
                while(rs.next()) {
                    string.append(guild.getTextChannelCache().getElementById(rs.getString("channelId")).getAsMention()).append(delimiter);
                }

                if(string.length() > 0) {
                    TextUtilities.removeLast(string, delimiter);
                } else {
                    string.append("`None`");
                }

                return string.toString();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return null;
            }
        }

        /**
         * Checks to see if a bind for a certain channel/module combination exists.
         *
         * @param guildId String
         * @param channelId String
         * @param moduleName String
         * @return boolean
         */
        public static boolean checkBind(String guildId, String channelId, String moduleName) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_module_binds` WHERE (`guildId` = ? AND `moduleName` = ?) OR (`guildId` = ? AND `moduleName` = '*')")) {

                stmt.setString(1, guildId);
                stmt.setString(2, moduleName);
                stmt.setString(3, guildId);

                ResultSet rs = stmt.executeQuery();
                int count = 0;
                while(rs.next()) {
                    if(rs.getString("channelId").equals(channelId)) {
                        return true;
                    }
                    count++;
                }

                return count < 1;

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return false;
            }
        }

        /**
         * Verifies all binds, removing any that are no longer valid.
         *
         * @param guild {@link Guild} object
         */
        public static void verifyBinds(Guild guild) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_module_binds` WHERE `guildId` = ?")) {

                stmt.setString(1, guild.getId());
                ResultSet rs = stmt.executeQuery();

                while(rs.next()) {
                    String channelId = rs.getString("channelId");
                    if(guild.getTextChannelCache().getElementById(channelId) == null) {
                        clearBindByChannel(channelId);
                    }
                }

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
            }
        }

        /**
         * Clears a module bind from the given channel.
         *
         * @param guild String
         * @param channel String
         * @param module String
         * @return boolean
         */
        private static boolean clearBind(String guild, String channel, String module) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_module_binds` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?")) {

                stmt.setString(1, guild);
                stmt.setString(2, channel);
                stmt.setString(3, module);

                if(!stmt.execute()) {
                    stmt.close();
                    conn.close();
                    return true;
                }

                return false;

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return false;
            }
        }

        /**
         * Clears all binds that are connected to a specific channel.
         *
         * @param channelId channelId string.
         */
        private static void clearBindByChannel(String channelId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_module_binds` WHERE `channelId` = ?")) {

                stmt.setString(1, channelId);
                stmt.execute();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
            }
        }

        /**
         * Removes all references of channels that are deleted.
         *
         * @param channel String id of the channel referenced
         */
        public static void cleanupReferences(String channel) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_module_binds` WHERE `channelId` = ?");
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `guilds_settings` SET `starboard` = null WHERE 'starboard' = ?");
                PreparedStatement stmt3 = conn.prepareStatement("UPDATE `guilds_settings` SET `comLog` = null WHERE 'comLog' = ?");
                PreparedStatement stmt4 = conn.prepareStatement("UPDATE `guilds_settings` SET `modLog` = null WHERE 'modLog' = ?")) {

                stmt.setString(1, channel);
                stmt.execute();
                stmt2.setString(1, channel);
                stmt2.execute();
                stmt3.setString(1, channel);
                stmt3.execute();
                stmt4.setString(1, channel);
                stmt4.execute();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
            }
        }
    }

}
