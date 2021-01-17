package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.commands.BindCommand;
import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EventCommand extends Command {
    private static final HashMap<String, ScheduledEvent> inProgressEmbeds = new HashMap<>();

    public EventCommand() {
        super("event", Yuuko.MODULES.get("utility"), 0, -1L, Arrays.asList("-event", "-event channel #channel", "-event new", "-event title <value>", "-event desc <value>", "-event time <yyyy-MM-dd HH:mm>", "-event slots <value>", "-event notify <boolean>", "-event publish", "-event cancel | <value>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            ArrayList<ScheduledEvent> scheduledEvents = DatabaseInterface.getEvents(e.getGuild().getId());
            StringBuilder eventsString = new StringBuilder();
            for(ScheduledEvent scheduledEvent : scheduledEvents) {
                eventsString.append("ID: `").append(scheduledEvent.id).append("` - ").append(scheduledEvent.title).append(" ~ `").append(scheduledEvent.timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy"))).append(" (").append(TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY))).append(")`\n");
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Events (" + scheduledEvents.size() + ")")
                    .setDescription(eventsString.toString())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String[] params = e.getParameters().split("\\s+", 2);
        switch(params[0].toLowerCase()) {
            case "new" -> {
                createEvent(e);
                return;
            }
            case "cancel" -> {
                cancelEvent(e, params);
                return;
            }
            case "publish" -> {
                publishEvent(e);
                return;
            }
            case "channel" -> {
                setChannel(e);
                return;
            }
        }

        // Parameter checking - everything past here requires at least 2 parameters (action, value)
        if(params.length < 2) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Missing Parameters")
                    .setDescription("That command requires more parameters than you provided.");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        if(!inProgressEmbeds.containsKey(e.getAuthor().getId())) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Missing Event")
                    .setDescription("There is no event to manipulate, use `" + e.getPrefix() + "event new` to get started!");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        ScheduledEvent scheduledEvent = inProgressEmbeds.get(e.getAuthor().getId());
        switch(params[0].toLowerCase()) {
            case "title" -> {
                scheduledEvent.title = params[1].length() > 256 ? params[1].substring(0, 255) : params[1];
                scheduledEvent.embedBuilder.setTitle(scheduledEvent.title);
                scheduledEvent.message.editMessage(scheduledEvent.embedBuilder.build()).queue();
            }
            case "desc" -> {
                scheduledEvent.description = params[1].length() > 2048 ? params[1].substring(0, 2047) : params[1];
                scheduledEvent.embedBuilder.setDescription(scheduledEvent.description);
                scheduledEvent.message.editMessage(scheduledEvent.embedBuilder.build()).queue();
            }
            case "time" -> {
                if(Sanitiser.isTimestamp(params[1]+":00")) {
                    scheduledEvent.timestamp = Timestamp.valueOf(params[1]+":00");
                    scheduledEvent.embedBuilder.getFields().remove(0);
                    scheduledEvent.embedBuilder.getFields().add(0, new MessageEmbed.Field("Scheduled", "`" + scheduledEvent.timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy")) + " (" + TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY)) + ")`", false));
                    scheduledEvent.message.editMessage(scheduledEvent.embedBuilder.build()).queue();
                    return;
                }

                EmbedBuilder about = new EmbedBuilder().setTitle("Incorrect Format")
                        .setDescription("The timestamp entered isn't valid, ensure the format is `yyyy-MM-dd HH:mm`");
                MessageDispatcher.reply(e, about.build());
            }
            case "slots" -> {
                if(Sanitiser.isNumber(params[1])) {
                    scheduledEvent.slots = Integer.parseInt(params[1]);
                    scheduledEvent.embedBuilder.getFields().remove(1);
                    scheduledEvent.embedBuilder.getFields().add(1, new MessageEmbed.Field("Participants (0/" + scheduledEvent.slots + ")", "`None`", true));
                    scheduledEvent.message.editMessage(scheduledEvent.embedBuilder.build()).queue();
                    return;
                }

                if(params[1].equals("-1")) {
                    scheduledEvent.slots = -1;
                    scheduledEvent.embedBuilder.getFields().remove(1);
                    scheduledEvent.embedBuilder.getFields().add(1, new MessageEmbed.Field("Participants (0)", "", true));
                    scheduledEvent.message.editMessage(scheduledEvent.embedBuilder.build()).queue();
                    return;
                }

                EmbedBuilder about = new EmbedBuilder().setTitle("Invalid Value")
                        .setDescription("The input value isn't valid, ensure you give a valid non-negative integer or `-1` to remove the limit.");
                MessageDispatcher.reply(e, about.build());
            }
            case "notify" -> {
                List<String> booleans = Arrays.asList("yes", "true", "1");
                if(booleans.contains(params[1])) {
                    scheduledEvent.notify = true;
                    scheduledEvent.embedBuilder.getFields().remove(2);
                    scheduledEvent.embedBuilder.getFields().add(2, new MessageEmbed.Field("Notify?", "`true`", true));
                } else {
                    scheduledEvent.notify = false;
                    scheduledEvent.embedBuilder.getFields().remove(2);
                    scheduledEvent.embedBuilder.getFields().add(2, new MessageEmbed.Field("Notify?", "`false`", true));
                }
                scheduledEvent.message.editMessage(scheduledEvent.embedBuilder.build()).queue();
            }
        }
    }

    /**
     * Subclass which contains all information for a given event - this class makes manipulation of events trivial before/after database retrieval.
     */
    private static class ScheduledEvent {
        public int id;
        public String guildId;
        public Message message;
        public String messageId;
        public EmbedBuilder embedBuilder;
        public String title = "Event";
        public String description = "";
        public int slots = -1;
        public Timestamp timestamp = Timestamp.from(Instant.now().plusSeconds(86400));
        public boolean notify = false;

        public ScheduledEvent(Message message, EmbedBuilder embedBuilder) {
            this.message = message;
            this.embedBuilder = embedBuilder;
        }
    }

    /**
     * Creates a new event using the template, this event isn't the final event but merely a visualisation of an event.
     * Once the user publishes an event, this event will no longer be accessible.
     * @param e {@link MessageEvent}
     */
    private void createEvent(MessageEvent e) {
        if(inProgressEmbeds.containsKey(e.getAuthor().getId())) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Event Creating Failed")
                    .setDescription("You already have an event in construction. Please cancel or publish that event before creating a new event!");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Event")
                .setDescription("This is your new event! Below are commands you can now use. " +
                        "\n`" + e.getPrefix() + "event title <value>` to set the title." +
                        "\n`" + e.getPrefix() + "event desc <value>` to set the description. (this message will be replaced)" +
                        "\n`" + e.getPrefix() + "event time <yyyy-MM-dd HH:mm>` to set the date/time of this event." +
                        "\n`" + e.getPrefix() + "event slots <value>` to set a number of slots. (default unlimited)" +
                        "\n`" + e.getPrefix() + "event notify <boolean>` to notify participants (default false)." +
                        "\n`" + e.getPrefix() + "event publish` to publish this event." +
                        "\n`" + e.getPrefix() + "event cancel` to cancel this event.")
                .addField("Scheduled", "`" + Timestamp.from(Instant.now().plusSeconds(86400)).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy")) + " (" + TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY)) + ")`", false)
                .addField("Participants (0)", "`None`", true)
                .addField("Notify?", "`false`", true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        e.getChannel().sendMessage(embed.build()).queue(s -> inProgressEmbeds.put(e.getAuthor().getId(), new ScheduledEvent(s, embed)));
    }

    /**
     * Cancels an event, if params size is only 1 and there is an event in construction, that will be cancelled.
     * If params is size of 2, the database will be searched and the event will be cancelled if it exists.
     * @param e {@link MessageEvent}
     * @param params String[]
     */
    private void cancelEvent(MessageEvent e, String[] params) {
        if(params.length == 1) {
            if(!inProgressEmbeds.containsKey(e.getAuthor().getId())) {
                EmbedBuilder about = new EmbedBuilder().setTitle("Unknown Event")
                        .setDescription("Unable to find event matching that ID.");
                MessageDispatcher.reply(e, about.build());
                return;
            }

            EmbedBuilder about = new EmbedBuilder().setTitle("Event Cancelled");
            MessageDispatcher.reply(e, about.build());
            inProgressEmbeds.get(e.getAuthor().getId()).message.delete().queue();
            inProgressEmbeds.remove(e.getAuthor().getId());
            return;
        }

        if(!Sanitiser.isNumber(params[1])) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Incorrect Format")
                    .setDescription("Event IDs must be non-negative integers, e.g: `12`");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        String messageId = DatabaseInterface.getEventMessage(Integer.parseInt(params[1]));
        if(messageId == null) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Unknown Event")
                    .setDescription("Unable to find event matching that ID.");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        DatabaseInterface.removeEvent(messageId);
        String channelId = GuildFunctions.getGuildSetting("events", e.getGuild().getId());
        if(channelId != null) {
            TextChannel textChannel = e.getGuild().getTextChannelById(channelId);
            if(textChannel != null) {
                textChannel.deleteMessageById(messageId).queue();
            }
        }

        EmbedBuilder about = new EmbedBuilder().setTitle("Event Cancelled")
                .setDescription("You have successfully cancelled event, `ID: " + params[1] + "`");
        MessageDispatcher.reply(e, about.build());
    }

    /**
     * Publishes an event to the set events channel. If there is no events channel, it will encourage the user to set one.
     * Also deletes the temporary visualisation created by the previous createEvent() function.
     * @param e {@link MessageEvent}
     */
    private void publishEvent(MessageEvent e) {
        String channelId = GuildFunctions.getGuildSetting("events", e.getGuild().getId());
        if(channelId == null) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Publish Failed")
                    .setDescription("Unable to find events channel, set it by using `" + e.getPrefix() + "event channel #channel`.");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        TextChannel textChannel = e.getGuild().getTextChannelById(channelId);
        if(textChannel != null) {
            ScheduledEvent scheduledEvent = inProgressEmbeds.get(e.getAuthor().getId());
            int eventId = DatabaseInterface.newEvent(e, scheduledEvent);
            scheduledEvent.embedBuilder.setFooter("ID: " + eventId);
            textChannel.sendMessage(scheduledEvent.embedBuilder.build()).queue(message -> {
                scheduledEvent.message.delete().queue(x -> inProgressEmbeds.remove(e.getAuthor().getId()));
                DatabaseInterface.updateEventMessage(eventId, message.getId());
                message.addReaction("✅").queue();
            });
        }
    }

    /**
     * Sets the channel in which all published events will be sent. If this channel is deleted, all events are also removed.
     * @param e {@link MessageEvent}
     */
    private void setChannel(MessageEvent e) {
        if(e.getMessage().getMentionedChannels().size() < 1) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Missing Channel")
                    .setDescription("You need to mention a channel, e.g: " + e.getChannel().getAsMention());
            MessageDispatcher.reply(e, about.build());
            return;
        }

        GuildFunctions.setGuildSettings("events", e.getMessage().getMentionedChannels().get(0).getId(), e.getGuild().getId());
        EmbedBuilder about = new EmbedBuilder().setTitle("Events")
                .setDescription("The events channel has been set to " + e.getMessage().getMentionedChannels().get(0).getAsMention());
        MessageDispatcher.reply(e, about.build());
    }

    /**
     * Processes reaction events on event messages, updating participants count/list.
     * @param e {@link GenericGuildMessageReactionEvent}
     */
    public static void processReaction(GenericGuildMessageReactionEvent e) {
        final String emote = e.getReactionEmote().getAsReactionCode();
        if(emote.equals("✅")) {
            HashMap<String, String> event = DatabaseInterface.getEvent(e.getMessageId());
            e.getChannel().retrieveMessageById(e.getMessageId()).queue(message -> {
                Optional<MessageReaction> reaction = message.getReactions().stream().filter(messageReaction -> messageReaction.getReactionEmote().getName().equals("✅")).findAny();
                reaction.ifPresent(messageReaction -> messageReaction.retrieveUsers().queue(users -> {
                    users.remove(Yuuko.BOT); // remove bot from users list

                    StringBuilder participantString = new StringBuilder();
                    users.stream().limit(10).forEach(user -> participantString.append("`").append(user.getAsTag()).append("`\n"));

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(event.get("title"))
                            .setDescription(event.get("description"))
                            .addField("Scheduled", "`" + Timestamp.valueOf(event.get("scheduled")).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy")) + " (" + TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY)) + ")`", false)
                            .addField("Participants " + (event.get("slots").equals("-1") ? "(" + users.size() + ")" : "(" + users.size() + "/" + event.get("slots") + ")"), participantString.toString(), true)
                            .addField("Notify?", "`" + event.get("notify") + "`", true)
                            .setFooter("ID: " + event.get("id"));
                    e.getChannel().editMessageById(e.getMessageId(), embed.build()).queue();
                }));
            });
        }
    }

    /**
     * Notifies users that events are about to start, then removes the event from the database.
     */
    public static void notifyEvents() {
        final long TEN_MINUTES_FUTURE = System.currentTimeMillis() + (10 * 60 * 1000);
        ArrayList<ScheduledEvent> scheduledEvents = DatabaseInterface.getAllEvents();
        scheduledEvents.forEach(scheduledEvent -> {
            if(scheduledEvent.notify && scheduledEvent.timestamp.getTime() <= TEN_MINUTES_FUTURE) {
                Guild guild = Yuuko.BOT.getJDA().getGuildById(scheduledEvent.guildId);
                if(guild != null) {
                    String channelId = GuildFunctions.getGuildSetting("events", scheduledEvent.guildId);
                    if(channelId != null) {
                        TextChannel textChannel = guild.getTextChannelById(channelId);
                        if(textChannel != null) {
                            textChannel.retrieveMessageById(scheduledEvent.messageId).queue(message -> {
                                Optional<MessageReaction> reaction = message.getReactions().stream().filter(messageReaction -> messageReaction.getReactionEmote().getName().equals("✅")).findAny();
                                reaction.ifPresent(messageReaction -> messageReaction.retrieveUsers().queue(users -> {
                                    users.remove(Yuuko.BOT); // remove bot from users list
                                    StringBuilder notificationString = new StringBuilder();
                                    notificationString.append(scheduledEvent.title).append(" `id: ").append(scheduledEvent.id).append("` is starting soon!\n\n");
                                    users.stream().limit(scheduledEvent.slots).forEach(user -> notificationString.append(user.getAsMention()).append(" "));
                                    textChannel.sendMessage(notificationString.toString()).queue(success -> DatabaseInterface.disableNotify(scheduledEvent.id));
                                }));
                            });
                        }
                    }
                }
            }
            // Purge expired events
            if(scheduledEvent.timestamp.getTime() < System.currentTimeMillis()) {
                DatabaseInterface.removeEvent(scheduledEvent.id);
            }
        });
    }

    public static class DatabaseInterface {
        /**
         * Adds a new event to the database.
         * @param messageEvent {@link MessageEvent}
         * @param scheduledEvent {@link ScheduledEvent}
         */
        public static int newEvent(MessageEvent messageEvent, ScheduledEvent scheduledEvent) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `guilds_events`(`guildId`, `messageId`, `eventTitle`, `eventDescription`, `eventSlots`, `eventScheduled`, `eventNotify`) VALUES (?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM `guilds_events` WHERE `guildId` = ? AND `messageId` = ?")) {

                stmt.setString(1, messageEvent.getGuild().getId());
                stmt.setString(2, scheduledEvent.message.getId());
                stmt.setString(3, scheduledEvent.title);
                stmt.setString(4, scheduledEvent.description);
                stmt.setInt(5, scheduledEvent.slots);
                stmt.setTimestamp(6, scheduledEvent.timestamp);
                stmt.setBoolean(7, scheduledEvent.notify);
                stmt.execute();

                stmt2.setString(1, messageEvent.getGuild().getId());
                stmt2.setString(2, scheduledEvent.message.getId());
                ResultSet resultSet = stmt2.executeQuery();

                if(resultSet.next()) {
                    return resultSet.getInt("eventId");
                }

                return -1;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return -1;
            }
        }

        /**
         * Returns an ArrayList of all events for that guild.
         * @return {@link ArrayList}
         */
        public static ArrayList<ScheduledEvent> getAllEvents() {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_events`")) {

                ResultSet resultSet = stmt.executeQuery();

                ArrayList<ScheduledEvent> scheduledEvents = new ArrayList<>();
                while(resultSet.next()) {
                    ScheduledEvent scheduledEvent = new ScheduledEvent(null, null);
                    scheduledEvent.guildId = resultSet.getString("guildId");
                    scheduledEvent.messageId = resultSet.getString("messageId");
                    scheduledEvent.title = resultSet.getString("eventTitle");
                    scheduledEvent.timestamp = resultSet.getTimestamp("eventScheduled");
                    scheduledEvent.id = resultSet.getInt("eventId");
                    scheduledEvent.notify = resultSet.getBoolean("eventNotify");
                    scheduledEvents.add(scheduledEvent);
                }

                return scheduledEvents;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return new ArrayList<>();
            }
        }

        /**
         * Returns an ArrayList of all events for that guild.
         * @param guildId String
         * @return {@link ArrayList}
         */
        public static ArrayList<ScheduledEvent> getEvents(String guildId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_events` WHERE `guildId` = ?")) {

                stmt.setString(1, guildId);
                ResultSet resultSet = stmt.executeQuery();

                ArrayList<ScheduledEvent> scheduledEvents = new ArrayList<>();
                while(resultSet.next()) {
                    ScheduledEvent scheduledEvent = new ScheduledEvent(null, null);
                    scheduledEvent.title = resultSet.getString("eventTitle");
                    scheduledEvent.timestamp = resultSet.getTimestamp("eventScheduled");
                    scheduledEvent.id = resultSet.getInt("eventId");
                    scheduledEvents.add(scheduledEvent);
                }

                return scheduledEvents;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return new ArrayList<>();
            }
        }

        /**
         * Returns an ArrayList of an event for that guild.
         * @param messageId String
         * @return {@link ArrayList}
         */
        public static HashMap<String, String> getEvent(String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_events` WHERE `messageId` = ?")) {

                stmt.setString(1, messageId);
                ResultSet resultSet = stmt.executeQuery();

                HashMap<String, String> event = new HashMap<>();
                if(resultSet.next()) {
                    event.put("id", resultSet.getString("eventId"));
                    event.put("title", resultSet.getString("eventTitle"));
                    event.put("description", resultSet.getString("eventDescription"));
                    event.put("slots", resultSet.getInt("eventSlots")+"");
                    event.put("scheduled", resultSet.getTimestamp("eventScheduled").toString()+"");
                    event.put("notify", resultSet.getBoolean("eventNotify")+"");
                }

                return event;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return new HashMap<>();
            }
        }

        /**
         * Returns an event using eventId.
         * @param eventId String
         * @return String
         */
        public static String getEventMessage(int eventId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_events` WHERE `eventId` = ?")) {

                stmt.setInt(1, eventId);
                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.next()) {
                    return resultSet.getString("messageId");
                }

                return null;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return null;
            }
        }

        public static int getEventSlots(String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT `eventSlots` FROM `guilds_events` WHERE `messageId` = ?")) {

                stmt.setString(1, messageId);
                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.next()) {
                    return resultSet.getInt("eventSlots");
                }

                return -1;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return -1;
            }
        }

        /**
         * Updates event message, which is set to the event builder initially.
         * @param eventId int
         * @param messageId String
         */
        public static void updateEventMessage(int eventId, String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_events` SET messageId = ? WHERE `eventId` = ?")) {

                stmt.setString(1, messageId);
                stmt.setInt(2, eventId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Disables notifications for an event - this is called automatically after a guild is notified of an event.
         * @param eventId int
         */
        public static void disableNotify(int eventId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_events` SET eventNotify = 0 WHERE `eventId` = ?")) {

                stmt.setInt(1, eventId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes an event from the database using the given eventId.
         * @param eventId int
         */
        public static void removeEvent(int eventId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_events` WHERE `eventId` = ?")) {

                stmt.setInt(1, eventId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes an event from the database using the given messageId.
         * @param messageId String
         */
        public static void removeEvent(String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_events` WHERE `messageId` = ?")) {

                stmt.setString(1, messageId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }
    }

}
