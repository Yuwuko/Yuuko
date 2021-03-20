package com.yuuko.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.io.entity.RequestProperty;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class TescoCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("tesco");
    private static final String BASE_URL = "https://dev.tescolabs.com/grocery/products/?query=";

    public TescoCommand() {
        super("tesco", 1, -1L, Arrays.asList("-tesco <product>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final String url = BASE_URL + Sanitiser.scrub(e.getParameters(), true) + "&offset=0&limit=1";
        final JsonObject json = new RequestHandler(url, new RequestProperty("Ocp-Apim-Subscription-Key", api.getKey())).getJsonObject();
        final JsonObject preData = json.get("uk").getAsJsonObject().get("ghs").getAsJsonObject().get("products").getAsJsonObject();

        if(preData.get("results").getAsJsonArray().size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
            MessageDispatcher.reply(e, embed.build());
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
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }

}
