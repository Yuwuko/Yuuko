package com.yuuko.modules.media.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LondonUndergroundCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("transportforlondon");
    private static final String BASE_URL = "https://api.tfl.gov.uk/line/mode/tube/status?app_id=" + api.getApplicationId() + "&app_key=" + api.getKey();

    public LondonUndergroundCommand() {
        super("underground", 0, -1L, Arrays.asList("-underground", "-underground <min>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final String json = new RequestHandler(BASE_URL).getString();
        List<LineManager> lineManager = new ObjectMapper().readValue(json, new TypeReference<>(){});
        StringBuilder reasons = new StringBuilder();

        int goodServices = 11;
        for(LineManager manager: lineManager) {
            if(!manager.getLineStatusString().equals("Good Service")) {
                reasons.append(manager.getLineStatusReason()).append("\n");
                goodServices--;
            }
        }

        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(I18n.getText(e, "title"))
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());

            for(LineManager line : lineManager) {
                embed.addField(line.getName(), line.getLineStatusString(), true);
            }

            embed.addBlankField(true);
            embed.addField("", reasons.toString(), false);
            MessageDispatcher.reply(e, embed.build());
        } else {

            if(goodServices == 11) {
                reasons.append(I18n.getText(e, "good_service"));
            } else if(goodServices > 0) {
                reasons.append(I18n.getText(e, "good_service_other"));
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(I18n.getText(e, "title"))
                    .addField("", reasons.toString(), false)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageDispatcher.reply(e, embed.build());
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
        private final List<LineStatus> lineStatuses = new ArrayList<>();

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
            TextUtilities.removeLast(reasons, "\n\n");

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
