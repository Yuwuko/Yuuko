package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.connection.DatabaseConnection;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.core.commands.BindCommand;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
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
    private static final List<String> eventParameters = Arrays.asList("new", "title", "desc", "time", "slots", "notify", "publish", "cancel");
    private static final HashMap<String, ScheduledEvent> inProgressEmbeds = new HashMap<>();

    public EventCommand() {
        super("event", 0, -1L, Arrays.asList("-event", "-event new", "-event title <value>", "-event desc <value>", "-event time <yyyy-MM-dd HH:mm>", "-event slots <value>", "-event notify <boolean>", "-event publish", "-event cancel | <value>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            StringBuilder eventsString = new StringBuilder();
            ArrayList<ScheduledEvent> scheduledEvents = DatabaseInterface.getEvents(context.getGuild().getId());
            for(ScheduledEvent scheduledEvent : scheduledEvents) {
                eventsString.append("ID: `").append(scheduledEvent.id).append("` - ").append(scheduledEvent.title).append(" ~ `").append(scheduledEvent.timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy"))).append(" (").append(TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY))).append(")`\n");
            }
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Events (" + scheduledEvents.size() + ")")
                    .setDescription(eventsString.toString())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String[] params = context.getParameters().split("\\s+", 2);
        params[0] = params[0].toLowerCase(); // make it lowercase now so we don't have to later

        // Silently fail if the user enters a parameter we don't handle.
        if(!eventParameters.contains(params[0])) {
            return;
        }

        // Only case where scheduled event isn't called at all.
        if(params[0].equals("new")) {
            createEvent(context);
            return;
        }

        // Only case which can accept both 1 and 2 parameters.
        if(params[0].equals("cancel")) {
            cancelEvent(context, params);
            return;
        }

        // Everything past here requires a pre-existing scheduled event.
        ScheduledEvent scheduledEvent = inProgressEmbeds.getOrDefault(context.getAuthor().getId(), null);
        if(scheduledEvent == null) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Missing Event")
                    .setDescription("There is no event to manipulate, use `" + context.getPrefix() + "event new` to get started!");
            MessageDispatcher.reply(context, about.build());
            return;
        }

        // Only case where scheduled event is needed, but only 1 parameter
        if(params[0].equals("publish")) {
            publishEvent(context);
            return;
        }

        // Parameter checking - everything past here requires at least 2 parameters (action, value)
        if(params.length < 2) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Missing Parameters")
                    .setDescription("That function requires more parameters than you provided. Use `" + context.getPrefix() + "help event` if you get stuck.");
            MessageDispatcher.reply(context, about.build());
            return;
        }

        switch(params[0]) {
            case "title" -> scheduledEvent.setTitle(params[1]).submitEdit();
            case "desc" -> scheduledEvent.setDescription(params[1]).submitEdit();
            case "notify" -> scheduledEvent.setNotify(Sanitiser.isBooleanTrue(params[1])).submitEdit();
            case "time" -> {
                if(!Sanitiser.isTimestamp(params[1]+":00")) {
                    EmbedBuilder about = new EmbedBuilder().setTitle("Invalid Value")
                            .setDescription("The timestamp entered isn't valid, ensure the format is `yyyy-MM-dd HH:mm`");
                    MessageDispatcher.reply(context, about.build());
                    return;
                }
                scheduledEvent.setTimestamp(Timestamp.valueOf(params[1]+":00")).submitEdit();
            }
            case "slots" -> {
                if(!Sanitiser.isNumeric(params[1])) {
                    EmbedBuilder about = new EmbedBuilder().setTitle("Invalid Value")
                            .setDescription("The input isn't valid, ensure you supply a non-negative integer, or 0, removing the limit.");
                    MessageDispatcher.reply(context, about.build());
                    return;
                }
                scheduledEvent.setSlots(Integer.parseInt(params[1])).submitEdit();
            }
            default -> {
            }
        }
    }

    /**
     * Clears inProgressEmbeds which are over an hour old - chances are they have been abandoned and are wasting memory.
     */
    public static void pruneEvents() {
        inProgressEmbeds.values().removeIf(scheduledEvent ->  (System.currentTimeMillis() - scheduledEvent.creationTime.getTime()) >= 3600000);
    }

    /**
     * Creates a new event using the template, this event isn't the final event but merely a visualisation of an event.
     * Once the user publishes an event, this event will no longer be accessible.
     * @param e {@link MessageEvent}
     */
    private void createEvent(MessageEvent e) {
        if(inProgressEmbeds.containsKey(e.getAuthor().getId())) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Event Creation Failed")
                    .setDescription("You already have an event in construction. Please cancel or publish that event before creating a new event!");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        ScheduledEvent scheduledEvent = new ScheduledEvent()
                .setTitle("Event")
                .setDescription("This is your new event! Below are commands you can now use. " +
                        "\n`" + e.getPrefix() + "event title <value>` to set the title." +
                        "\n`" + e.getPrefix() + "event desc <value>` to set the description. (this message will be replaced)" +
                        "\n`" + e.getPrefix() + "event time <yyyy-MM-dd HH:mm>` to set the date/time of this event." +
                        "\n`" + e.getPrefix() + "event slots <value>` to set a number of slots. (default 0 [unlimited])" +
                        "\n`" + e.getPrefix() + "event notify <boolean>` to notify participants (default false)." +
                        "\n`" + e.getPrefix() + "event publish` to publish this event." +
                        "\n`" + e.getPrefix() + "event cancel` to cancel this event.")
                .setTimestamp(Timestamp.from(Instant.now().plusSeconds(86400)))
                .setSlots(0)
                .setNotify(false)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag());
        e.getChannel().sendMessage(scheduledEvent.getEmbed()).queue(s -> inProgressEmbeds.put(e.getAuthor().getId(), scheduledEvent.setMessage(s)));
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
                        .setDescription("Unable to find event.");
                MessageDispatcher.reply(e, about.build());
                return;
            }

            EmbedBuilder about = new EmbedBuilder().setTitle("Event Cancelled");
            MessageDispatcher.reply(e, about.build());
            inProgressEmbeds.get(e.getAuthor().getId()).message.delete().queue();
            inProgressEmbeds.remove(e.getAuthor().getId());
            return;
        }

        if(!Sanitiser.isNumeric(params[1])) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Incorrect Format")
                    .setDescription("Event IDs must be non-negative integers, e.g: `12`");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        String messageId = DatabaseInterface.getEventMessage(e.getGuild().getId(), Integer.parseInt(params[1]));
        if(messageId == null) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Unknown Event")
                    .setDescription("Unable to find event matching that ID.");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        DatabaseInterface.removeEvent(e.getGuild().getId(), messageId);
        String channelId = GuildFunctions.getGuildSetting("eventchannel", e.getGuild().getId());
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
        String channelId = GuildFunctions.getGuildSetting("eventchannel", e.getGuild().getId());
        if(channelId == null) {
            EmbedBuilder about = new EmbedBuilder().setTitle("Publish Failed")
                    .setDescription("Unable to find events channel, set it by using `" + e.getPrefix() + "eventchannel #channel`.");
            MessageDispatcher.reply(e, about.build());
            return;
        }

        TextChannel textChannel = e.getGuild().getTextChannelById(channelId);
        if(textChannel != null) {
            ScheduledEvent scheduledEvent = inProgressEmbeds.get(e.getAuthor().getId());
            int eventId = DatabaseInterface.newEvent(e, scheduledEvent);
            if(eventId != -1) {
                scheduledEvent.setFooter("ID: " + eventId);
                textChannel.sendMessage(scheduledEvent.getEmbed()).queue(message -> {
                    scheduledEvent.message.delete().queue(x -> inProgressEmbeds.remove(e.getAuthor().getId()));
                    DatabaseInterface.updateEventMessage(eventId, message.getId());
                    message.addReaction("✅").queue();
                });
                return;
            }
            EmbedBuilder about = new EmbedBuilder().setTitle("Publish Failed")
                    .setDescription("There was an issue publishing your event... if this happens again please contact developer.");
            MessageDispatcher.reply(e, about.build());
        }
    }

    /**
     * Processes reaction events on event messages, updating participants count/list.
     * @param e {@link GenericGuildMessageReactionEvent}
     */
    public static void processReaction(GenericGuildMessageReactionEvent e) {
        if(e.getReactionEmote().getAsReactionCode().equals("✅")) {
            ScheduledEvent event = DatabaseInterface.getEvent(e.getMessageId());
            if(event.timestamp != null) {
                e.getChannel().retrieveMessageById(e.getMessageId()).queue(message -> {
                    Optional<MessageReaction> reaction = message.getReactions().stream().filter(messageReaction -> messageReaction.getReactionEmote().getName().equals("✅")).findAny();
                    reaction.ifPresent(messageReaction -> messageReaction.retrieveUsers().queue(users -> {
                        users.remove(Yuuko.BOT); // remove bot from users list
                        StringBuilder participantString = new StringBuilder();
                        users.stream().limit(10).forEach(user -> participantString.append("`").append(user.getAsTag()).append("`\n"));
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(event.title)
                                .setDescription(event.description)
                                .addField("Scheduled", "`" + event.timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy")) + " (" + TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY)) + ")`", false)
                                .addField("Participants " + ((event.slots == 0) ? "(" + users.size() + ")" : "(" + users.size() + "/" + event.slots + ")"), participantString.toString(), true)
                                .addField("Notify?", "`" + event.notify + "`", true)
                                .setFooter("ID: " + event.id);
                        e.getChannel().editMessageById(e.getMessageId(), embed.build()).queue();
                    }));
                });
            }
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
                    String channelId = GuildFunctions.getGuildSetting("eventchannel", scheduledEvent.guildId);
                    if(channelId != null) {
                        TextChannel textChannel = guild.getTextChannelById(channelId);
                        if(textChannel != null) {
                            textChannel.retrieveMessageById(scheduledEvent.messageId).queue(message -> {
                                Optional<MessageReaction> reaction = message.getReactions().stream().filter(messageReaction -> messageReaction.getReactionEmote().getName().equals("✅")).findAny();
                                reaction.ifPresent(messageReaction -> messageReaction.retrieveUsers().queue(users -> {
                                    StringBuilder notificationString = new StringBuilder();
                                    users.stream().filter(user -> user != Yuuko.BOT).limit((scheduledEvent.slots == 0) ? users.size() : scheduledEvent.slots).forEach(user -> {
                                        if((notificationString.length() + user.getAsMention().length() + 1) < 2048) {
                                            notificationString.append(user.getAsMention()).append(" ");
                                        }
                                    });
                                    MessageBuilder messageBuilder = new MessageBuilder()
                                            .setContent(notificationString.toString())
                                            .setEmbed(new EmbedBuilder().setTitle(scheduledEvent.title).setDescription("Starting in 10 minutes!").build());
                                    message.reply(messageBuilder.build()).queue(success -> DatabaseInterface.disableNotify(scheduledEvent.id));
                                }));
                            });
                        }
                    }
                }
            }
            // Purge expired events
            if(scheduledEvent.timestamp.getTime() < System.currentTimeMillis()) {
                DatabaseInterface.removeEvent(scheduledEvent.guildId, scheduledEvent.id);
            }
        });
    }

    /**
     * Subclass which contains all information for a given event - this class makes manipulation of events trivial before/after database retrieval.
     */
    private static class ScheduledEvent {
        private int id;
        private String guildId;
        private Message message;
        private String messageId;
        private String title;
        private String description;
        private Timestamp timestamp;
        private int slots;
        private boolean notify = false;
        private String footer;
        private final Timestamp creationTime = Timestamp.from(Instant.now()); // used to purge abandoned events from cache

        public ScheduledEvent setId(int id) {
            this.id = id;
            return this;
        }

        public ScheduledEvent setGuildId(String guildId) {
            this.guildId = guildId;
            return this;
        }

        public ScheduledEvent setMessage(Message message) {
            this.message = message;
            this.messageId = message.getId();
            return this;
        }

        public ScheduledEvent setMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public ScheduledEvent setTitle(String title) {
            this.title = (title.length() > 256) ? title.substring(0, 255) : title; // 256 max length of embed title
            return this;
        }

        public ScheduledEvent setDescription(String description) {
            this.description = (description.length() > 2048) ? description.substring(0, 2047) : description; // 2048 max length of embed description
            return this;
        }

        public ScheduledEvent setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ScheduledEvent setSlots(int slots) {
            this.slots = Math.max(slots, 0); // anything less than 1 equals infinite
            return this;
        }

        public ScheduledEvent setNotify(boolean notify) {
            this.notify = notify;
            return this;
        }

        public ScheduledEvent setFooter(String footer) {
            this.footer = (footer.length() > 2048) ? footer.substring(0, 2047) : footer; // 2048 max length of embed footer
            return this;
        }

        public void submitEdit() {
            message.editMessage(getEmbed()).queue();
        }

        public MessageEmbed getEmbed() {
            return new EmbedBuilder().setTitle(title)
                    .setDescription(description)
                    .addField("Scheduled", "`" + timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mma E dd MMM yy")) + " (" + TimeZone.getTimeZone("Europe/London").getDisplayName(false, TimeZone.SHORT, Locale.getDefault(Locale.Category.DISPLAY)) + ")`", false)
                    .addField("Participants " + ((slots == 0) ? "(0)" : "(0/" + slots + ")"), "`none`", true)
                    .addField("Notify?", "`" + notify + "`", true)
                    .setFooter(footer)
                    .build();
        }
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
                    ScheduledEvent scheduledEvent = new ScheduledEvent()
                            .setId(resultSet.getInt("eventId"))
                            .setGuildId(resultSet.getString("guildId"))
                            .setMessageId(resultSet.getString("messageId"))
                            .setTitle(resultSet.getString("eventTitle"))
                            .setTimestamp(resultSet.getTimestamp("eventScheduled"))
                            .setNotify(resultSet.getBoolean("eventNotify"));
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
                    ScheduledEvent scheduledEvent = new ScheduledEvent()
                            .setId(resultSet.getInt("eventId"))
                            .setTitle(resultSet.getString("eventTitle"))
                            .setTimestamp(resultSet.getTimestamp("eventScheduled"));
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
         * @return {@link ScheduledEvent}
         */
        public static ScheduledEvent getEvent(String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_events` WHERE `messageId` = ?")) {

                stmt.setString(1, messageId);
                ResultSet resultSet = stmt.executeQuery();

                ScheduledEvent event = new ScheduledEvent();
                if(resultSet.next()) {
                    event.setId(resultSet.getInt("eventId"))
                            .setTitle(resultSet.getString("eventTitle"))
                            .setDescription(resultSet.getString("eventDescription"))
                            .setSlots(resultSet.getInt("eventSlots"))
                            .setTimestamp(resultSet.getTimestamp("eventScheduled"))
                            .setNotify(resultSet.getBoolean("eventNotify"));
                }

                return event;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return new ScheduledEvent();
            }
        }

        /**
         * Returns an event using eventId. guildId is used as a guard against people trying to cancel arbitrary events.
         * @param guildId String
         * @param eventId int
         * @return String
         */
        public static String getEventMessage(String guildId, int eventId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_events` WHERE `guildId` = ? AND eventId` = ?")) {

                stmt.setString(1, guildId);
                stmt.setInt(2, eventId);
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

        /**
         * Returns number of event slots for the given eventId.
         * @param eventId int
         * @return int
         */
        public static int getEventSlots(int eventId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT `eventSlots` FROM `guilds_events` WHERE `eventId` = ?")) {

                stmt.setInt(1, eventId);
                ResultSet resultSet = stmt.executeQuery();

                if(resultSet.next()) {
                    return resultSet.getInt("eventSlots");
                }

                return 0;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return 0;
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
         * Removes an event from the database using the given guildId & eventId.
         * @param guildId String
         * @param eventId int
         */
        public static void removeEvent(String guildId, int eventId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_events` WHERE `guildId` = ? AND `eventId` = ?")) {

                stmt.setString(1, guildId);
                stmt.setInt(2, eventId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes an event from the database using the given guildId & messageId.
         * @param guildId String
         * @param messageId String
         */
        public static void removeEvent(String guildId, String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_events` WHERE `guildId` = ? AND `messageId` = ?")) {

                stmt.setString(1, guildId);
                stmt.setString(2, messageId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", BindCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }
    }

}
