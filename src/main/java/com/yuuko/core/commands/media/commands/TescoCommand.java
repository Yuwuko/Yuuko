package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.io.entity.RequestProperty;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class TescoCommand extends Command {
    private static final String BASE_URL = "https://dev.tescolabs.com/grocery/products/?query=";

    public TescoCommand() {
        super("tesco", Configuration.MODULES.get("media"), 1, -1L, Arrays.asList("-tesco <product>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            final String url = BASE_URL + Sanitiser.scrub(e.getParameters(), true) + "&offset=0&limit=1";
            final JsonObject json = new RequestHandler(url, new RequestProperty("Ocp-Apim-Subscription-Key", Utilities.getApiKey("tesco"))).getJsonObject();
            final JsonObject preData = json.get("uk").getAsJsonObject().get("ghs").getAsJsonObject().get("products").getAsJsonObject();

            if(preData.get("results").getAsJsonArray().size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = preData.get("results").getAsJsonArray().get(0).getAsJsonObject();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(data.get("name").getAsString())
                    .setThumbnail(data.get("image").getAsString())
                    .setDescription(((data.has("description") && data.get("description").getAsJsonArray().size() > 0) ? data.get("description").getAsJsonArray().get(0).toString() : "No description available."))
                    .addField("Price", "£" + BigDecimal.valueOf(data.get("price").getAsDouble()).setScale(2, RoundingMode.HALF_UP) + " (£" + BigDecimal.valueOf(data.get("unitprice").getAsDouble()).setScale(2, RoundingMode.HALF_UP) + "/" + data.get("UnitQuantity").getAsString() + ")", true)
                    .addField("Weight", BigDecimal.valueOf(data.get("ContentsQuantity").getAsDouble()).setScale(2, RoundingMode.HALF_UP) + data.get("ContentsMeasureType").getAsString(), true)
                    .addField("Quantity", data.get("UnitOfSale").getAsString() + "", true)
                    .addField("Department", data.get("superDepartment").getAsString() + " (" + data.get("department").getAsString() + ")", true)
                    .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
