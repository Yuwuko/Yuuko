package com.yuuko.core.commands.world.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.commands.world.tfl.LineManager;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Utilities;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class LondonUndergroundCommand extends Command {

    public LondonUndergroundCommand() {
        super("londonunderground", WorldModule.class, 0, new String[]{"-londonunderground", "-londonunderground <min>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            // Buffers JSON from the given URL and the uses ObjectMapper to turn it into usable Java objects.
            String json = new JsonBuffer("https://api.tfl.gov.uk/line/mode/tube/status?app_id=" + Utilities.getApiApplicationId("transportforlondon") + "&app_key=" + Utilities.getApiKey("transportforlondon"), "default", "default").getAsString();
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

            if(e.getCommand().length == 1) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("London Underground Status")
                        .setTimestamp(Instant.now())
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

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
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now());
                MessageHandler.sendMessage(e, embed.build());
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }

    }

}
