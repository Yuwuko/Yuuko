package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class UKParliamentPetitionCommand extends Command {
    private static final String BASE_URL = "https://petition.parliament.uk/petitions/";

    public UKParliamentPetitionCommand() {
        super("petition", Config.MODULES.get("media"), 0, -1L, Arrays.asList("-petition <id>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            if(e.hasParameters()) {
                final String url = BASE_URL + Sanitiser.scrub(e.getParameters(), true) + ".json";
                final JsonObject json = new RequestHandler(url).getJsonObject();

                if(json.has("error")) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
                    MessageDispatcher.reply(e, embed.build());
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
                        .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageDispatcher.reply(e, embed.build());
            } else {

                JsonObject json = new RequestHandler("https://petition.parliament.uk/petitions.json").getJsonObject();

                if(json.has("error")) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("There was a problem retrieving the main set of petitions.");
                    MessageDispatcher.reply(e, embed.build());
                    return;
                }

                JsonArray data = json.get("data").getAsJsonArray();

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("UK Parliament Petitions", "https://petition.parliament.uk/petitions")
                        .setDescription("Here is a list of the top ten open petitions, use `" + Utilities.getServerPrefix(e.getGuild()) + "petition <id>` to get more information about a specific petition.")
                        .setTimestamp(Instant.now())
                        .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

                int i = 0; // We only need 10 results,
                for(JsonElement element: data) {
                    JsonObject obj = element.getAsJsonObject();
                    embed.addField(obj.getAsJsonObject("attributes").get("action").getAsString(), "ID: [" + obj.get("id").getAsString() + "](https://petition.parliament.uk/petitions/" + obj.get("id").getAsString() + "), Signatures: " + TextUtilities.formatInteger(obj.getAsJsonObject("attributes").get("signature_count").getAsString()), false);
                    if(i++ > 8) {
                        break;
                    }
                }

                MessageDispatcher.reply(e, embed.build());
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
