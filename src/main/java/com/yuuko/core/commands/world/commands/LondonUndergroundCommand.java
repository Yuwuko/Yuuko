package com.yuuko.core.commands.world.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LondonUndergroundCommand extends Command {

    public LondonUndergroundCommand() {
        super("underground", WorldModule.class, 0, Arrays.asList("-underground", "-underground <min>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            final String url = "https://api.tfl.gov.uk/line/mode/tube/status?app_id=" + Utilities.getApiApplicationId("transportforlondon") + "&app_key=" + Utilities.getApiKey("transportforlondon");
            final String json = new RequestHandler(url).getString();

            ArrayList<LineManager> lineManager = new ObjectMapper().readValue(json, new TypeReference<List<LineManager>>(){});

            // Build string for reasons why line doesn't have good service.
            StringBuilder reasons = new StringBuilder();

            int goodServices = 11;
            for(LineManager manager: lineManager) {
                if(!manager.getLineStatusString().equals("Good Service")) {
                    reasons.append(manager.getLineStatusReason()).append("\n");
                    goodServices--;
                }
            }

            if(e.getCommand().size() == 1) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("London Underground Status")
                        .setTimestamp(Instant.now())
                        .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

                for(LineManager line: lineManager) {
                    embed.addField(line.getName(), line.getLineStatusString(), true);
                }

                embed.addBlankField(true);
                embed.addField("", reasons.toString(), false);
                MessageHandler.sendMessage(e, embed.build());

            } else {

                if(goodServices == 11) {
                    reasons.append("GOOD SERVICE on all lines.");
                } else if(goodServices > 0) {
                    reasons.append("GOOD SERVICE on all other lines.");
                }

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("London Underground Status (Minified)")
                        .addField("", reasons.toString(), false)
                        .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());
                MessageHandler.sendMessage(e, embed.build());
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "name",
            "lineStatuses"
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LineManager {

        @JsonProperty("name")
        private String name;
        @JsonProperty("lineStatuses")
        private List<LineStatus> lineStatuses = new ArrayList<>();

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("lineStatuses")
        String getLineStatusString() {
            StringBuilder statuses = new StringBuilder();

            for(LineStatus line: lineStatuses) {
                statuses.append(line.getStatusSeverityDescription()).append("\n");
            }

            int index = statuses.lastIndexOf("\n");
            statuses.replace(index, index + 1, "");

            return statuses.toString();
        }

        String getLineStatusReason() {
            StringBuilder reasons = new StringBuilder();
            String previous = "";

            for(LineStatus line: lineStatuses) {
                if(line.getReason() != null && !previous.equals(name)) {
                    reasons.append("**").append(line.getReason()).append("\n\n");
                    previous = name;
                }
            }
            TextUtilities.removeLastOccurrence(reasons, "\n\n");

            return reasons.toString().replace(":", "**:");
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "reason",
            "statusSeverityDescription"
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LineStatus {

        @JsonProperty("reason")
        private String reason;
        @JsonProperty("statusSeverityDescription")
        private String statusSeverityDescription;

        @JsonProperty("reason")
        String getReason() { return reason; }

        @JsonProperty("statusSeverityDescription")
        String getStatusSeverityDescription() {
            return statusSeverityDescription;
        }
    }

}
