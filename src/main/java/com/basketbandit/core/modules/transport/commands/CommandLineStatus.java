package com.basketbandit.core.modules.transport.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.transport.tfl.LineManager;
import com.basketbandit.core.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommandLineStatus extends Command {

    public CommandLineStatus() {
        super("linestatus", "com.basketbandit.core.modules.transport.ModuleTransport", new String[]{"-linestatus", "-linestatus [min]"}, null);
    }

    public CommandLineStatus(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            // Buffers JSON from the given URL and the uses ObjectMapper to turn it into usable Java objects.
            String json = Utils.bufferJson("https://api.tfl.gov.uk/line/mode/tube/status?app_id=" + Configuration.TFL_ID + "&app_key=" + Configuration.TFL_API);
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
                        .setColor(Color.RED)
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
                        .setFooter("Version: " + Configuration.VERSION + ", Data provided by tfl.gov.uk", e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                Utils.sendMessage(e, embed.build());

            } else {

                if(goodServices == 11) {
                    reasons.append("GOOD SERVICE on all lines.");
                } else if(goodServices > 0) {
                    reasons.append("GOOD SERVICE on all other lines.");
                }

                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("Tube Line Status (Minified) - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")))
                        .addField("", reasons.toString(), false)
                        .setFooter("Version: " + Configuration.VERSION + ", Data provided by tfl.gov.uk", e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                Utils.sendMessage(e, embed.build());
            }

        } catch(Exception ex) {
            Utils.sendMessage(e, "There was an issue processing your request.");
            ex.printStackTrace();
        }

    }

}
