package com.yuuko.core.commands.world.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;

public class UKParliamentPetitionCommand extends Command {

    public UKParliamentPetitionCommand() {
        super("petition", WorldModule.class, 1, new String[]{"-petition <id>"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            JsonObject json = new JsonBuffer("https://petition.parliament.uk/petitions/" + command[1] , "default", "default").getAsJsonObject();

            if(json.has("error")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Petition **_" + command[1] + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = json.get("data").getAsJsonObject();
            JsonObject attributes = data.getAsJsonObject("attributes");

            String responseThreshold = !attributes.get("response_threshold_reached_at").isJsonNull() ? TextUtilities.formatDate(attributes.get("response_threshold_reached_at").getAsString()) : "Not Reached";
            String responseDate = !attributes.get("government_response_at").isJsonNull() ? TextUtilities.formatDate(attributes.get("government_response_at").getAsString()) : "None";
            String debateThreshold = !attributes.get("debate_threshold_reached_at").isJsonNull() ? TextUtilities.formatDate(attributes.get("debate_threshold_reached_at").getAsString()) : "Not Reached";
            String debateDate = !attributes.get("scheduled_debate_date").isJsonNull() ? attributes.get("scheduled_debate_date").getAsString() : "None";
            String governmentResponse = !attributes.get("government_response").isJsonNull() ? attributes.get("government_response").getAsJsonObject().get("summary").getAsString() : "None";

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("UK Parliament Petition #" + data.get("id").getAsString() + " (" + attributes.get("state").getAsString() + ")", "https://petition.parliament.uk/petitions/" + data.get("id").getAsString())
                    .setTitle(attributes.get("action").getAsString())
                    .setDescription(attributes.get("background").getAsString())
                    .addField("Petition Opened", TextUtilities.formatDate(attributes.get("opened_at").getAsString()), true)
                    .addField("Signature Count", TextUtilities.formatInteger(attributes.get("signature_count").getAsString()), true)
                    .addBlankField(true)
                    .addField("Response Threshold", responseThreshold, true)
                    .addField("Response Date", responseDate, true)
                    .addBlankField(true)
                    .addField("Debate Threshold", debateThreshold, true)
                    .addField("Debate Date", debateDate, true)
                    .addBlankField(true)
                    .addField("Government Response Summary", governmentResponse, false)
                    .setTimestamp(Instant.now())
                    .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
