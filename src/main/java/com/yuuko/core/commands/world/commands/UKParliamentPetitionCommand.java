package com.yuuko.core.commands.world.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;

public class UKParliamentPetitionCommand extends Command {

    public UKParliamentPetitionCommand() {
        super("petition", WorldModule.class, 0, new String[]{"-petition <id>"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {

            if(command.length > 1) {
                JsonObject json = new JsonBuffer("https://petition.parliament.uk/petitions/" + command[1] + ".json", "default", "default").getAsJsonObject();

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
                        .setAuthor("UK Parliament Petition #" + data.get("id").getAsString() + " (" + attributes.get("state").getAsString() + ")")
                        .setTitle(attributes.get("action").getAsString(), "https://petition.parliament.uk/petitions/" + data.get("id").getAsString())
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
            } else {

                JsonObject json = new JsonBuffer("https://petition.parliament.uk/petitions.json", "default", "default").getAsJsonObject();

                if(json.has("error")) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("There was a problem retrieving the main set of petitions.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                JsonArray data = json.get("data").getAsJsonArray();

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("UK Parliament Petitions", "https://petition.parliament.uk/petitions")
                        .setDescription("Here is a list of the top ten open petitions, use `" + Utilities.getServerPrefix(e.getGuild()) + "petition <id>` to get more information about a specific petition.")
                        .setTimestamp(Instant.now())
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

                int i = 0; // We only need 10 results,
                for(JsonElement element: data) {
                    JsonObject obj = element.getAsJsonObject();
                    embed.addField(obj.getAsJsonObject("attributes").get("action").getAsString(), "ID: [" + obj.get("id").getAsString() + "](https://petition.parliament.uk/petitions/" + obj.get("id").getAsString() + "), Signatures: " + TextUtilities.formatInteger(obj.getAsJsonObject("attributes").get("signature_count").getAsString()), false);
                    if(i++ > 8) {
                        break;
                    }
                }

                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
