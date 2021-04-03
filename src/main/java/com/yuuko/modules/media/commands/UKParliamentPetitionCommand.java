package com.yuuko.modules.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class UKParliamentPetitionCommand extends Command {
    private static final String BASE_URL = "https://petition.parliament.uk/petitions/";

    public UKParliamentPetitionCommand() {
        super("petition", 0, -1L, Arrays.asList("-petition <id>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(e.hasParameters()) {
            final String url = BASE_URL + Sanitiser.scrub(e.getParameters(), true) + ".json";
            final JsonObject json = new RequestHandler(url).getJsonObject();

            if(json == null || json.isJsonNull() || json.has("error")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "no_results")).setDescription(I18n.getText(e, "no_results_desc").formatted(e.getParameters()));
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
                    .setAuthor(I18n.getText(e, "title").formatted(data.get("id").getAsString(), attributes.get("state").getAsString()))
                    .setTitle(attributes.get("action").getAsString(), "https://petition.parliament.uk/petitions/" + data.get("id").getAsString())
                    .setDescription(attributes.get("background").getAsString())
                    .addField(I18n.getText(e, "opened"), TextUtilities.formatDate(attributes.get("opened_at").getAsString()), true)
                    .addField(I18n.getText(e, "count"), TextUtilities.formatInteger(attributes.get("signature_count").getAsString()), true)
                    .addBlankField(true)
                    .addField(I18n.getText(e, "response_threshold"), responseThreshold, true)
                    .addField(I18n.getText(e, "response_date"), responseDate, true)
                    .addBlankField(true)
                    .addField(I18n.getText(e, "debate_threshold"), debateThreshold, true)
                    .addField(I18n.getText(e, "debate_date"), debateDate, true)
                    .addBlankField(true)
                    .addField(I18n.getText(e, "gov_response"), governmentResponse, false)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(e, embed.build());
        } else {

            JsonObject json = new RequestHandler("https://petition.parliament.uk/petitions.json").getJsonObject();

            if(json.has("error")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "no_results")).setDescription(I18n.getText(e, "no_results_desc").formatted(e.getParameters()));
                MessageDispatcher.reply(e, embed.build());
                return;
            }

            JsonArray data = json.get("data").getAsJsonArray();
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(I18n.getText(e, "title_ex"), "https://petition.parliament.uk/petitions")
                    .setDescription(I18n.getText(e, "desc").formatted(e.getPrefix()))
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());

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
    }
}
