package com.yuuko.core.commands.world.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.commands.world.tfl.LineManager;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Utils;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LineStatusCommand extends Command {

    public LineStatusCommand() {
        super("linestatus", WorldModule.class, 0, new String[]{"-linestatus", "-linestatus [min]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            // Buffers JSON from the given URL and the uses ObjectMapper to turn it into usable Java objects.
            String json = new JsonBuffer("https://api.tfl.gov.uk/line/mode/tube/status?app_id=" + Utils.getApiApplicationId("transportforlondon") + "&app_key=" + Utils.getApiKey("transportforlondon"), "default", "default", null, null).getAsString();
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

            if(command.length == 1) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Tube Line Status - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")))
                        .addField(lineManager.get(0).getName(), lineManager.get(0).getLineStatusString(), true)
                        .addField(lineManager.get(1).getName(), lineManager.get(1).getLineStatusString(), true)
                        .addField(lineManager.get(2).getName(), lineManager.get(2).getLineStatusString(), true)
                        .addField(lineManager.get(3).getName(), lineManager.get(3).getLineStatusString(), true)
                        .addField(lineManager.get(4).getName(), lineManager.get(4).getLineStatusString(), true)
                        .addField(lineManager.get(5).getName(), lineManager.get(5).getLineStatusString(), true)
                        .addField(lineManager.get(6).getName(), lineManager.get(6).getLineStatusString(), true)
                        .addField(lineManager.get(7).getName(), lineManager.get(7).getLineStatusString(), true)
                        .addField(lineManager.get(8).getName(), lineManager.get(8).getLineStatusString(), true)
                        .addField(lineManager.get(9).getName(), lineManager.get(9).getLineStatusString(), true)
                        .addField(lineManager.get(10).getName(), lineManager.get(10).getLineStatusString(), true)
                        .addField("", "", true)
                        .addField("", reasons.toString(), false)
                        .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());

            } else {

                if(goodServices == 11) {
                    reasons.append("GOOD SERVICE on all lines.");
                } else if(goodServices > 0) {
                    reasons.append("GOOD SERVICE on all other lines.");
                }

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Tube Line Status (Minified) - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")))
                        .addField("", reasons.toString(), false)
                        .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
