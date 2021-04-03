package com.yuuko.modules.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
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
    public void onCommand(MessageEvent context) throws Exception {
        if(context.hasParameters()) {
            final String url = BASE_URL + Sanitiser.scrub(context.getParameters(), true) + ".json";
            final JsonObject json = new RequestHandler(url).getJsonObject();

            if(json == null || json.isJsonNull() || json.has("error")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results")).setDescription(context.i18n( "no_results_desc").formatted(context.getParameters()));
                MessageDispatcher.reply(context, embed.build());
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
                    .setAuthor(context.i18n( "title").formatted(data.get("id").getAsString(), attributes.get("state").getAsString()))
                    .setTitle(attributes.get("action").getAsString(), "https://petition.parliament.uk/petitions/" + data.get("id").getAsString())
                    .setDescription(attributes.get("background").getAsString())
                    .addField(context.i18n( "opened"), TextUtilities.formatDate(attributes.get("opened_at").getAsString()), true)
                    .addField(context.i18n( "count"), TextUtilities.formatInteger(attributes.get("signature_count").getAsString()), true)
                    .addBlankField(true)
                    .addField(context.i18n( "response_threshold"), responseThreshold, true)
                    .addField(context.i18n( "response_date"), responseDate, true)
                    .addBlankField(true)
                    .addField(context.i18n( "debate_threshold"), debateThreshold, true)
                    .addField(context.i18n( "debate_date"), debateDate, true)
                    .addBlankField(true)
                    .addField(context.i18n( "gov_response"), governmentResponse, false)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, embed.build());
        } else {

            JsonObject json = new RequestHandler("https://petition.parliament.uk/petitions.json").getJsonObject();

            if(json.has("error")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results")).setDescription(context.i18n( "no_results_desc").formatted(context.getParameters()));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            JsonArray data = json.get("data").getAsJsonArray();
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "title_ex"), "https://petition.parliament.uk/petitions")
                    .setDescription(context.i18n( "desc").formatted(context.getPrefix()))
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());

            int i = 0; // We only need 10 results,
            for(JsonElement element: data) {
                JsonObject obj = element.getAsJsonObject();
                embed.addField(obj.getAsJsonObject("attributes").get("action").getAsString(), "ID: [" + obj.get("id").getAsString() + "](https://petition.parliament.uk/petitions/" + obj.get("id").getAsString() + "), Signatures: " + TextUtilities.formatInteger(obj.getAsJsonObject("attributes").get("signature_count").getAsString()), false);
                if(i++ > 8) {
                    break;
                }
            }

            MessageDispatcher.reply(context, embed.build());
        }
    }
}
