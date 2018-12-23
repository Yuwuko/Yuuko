package com.yuuko.core.modules.world.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import com.yuuko.core.utils.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CommandTesco extends Command {

    public CommandTesco() {
        super("tesco", "com.yuuko.core.modules.world.ModuleWorld", 1, new String[]{"-tesco [product]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            JsonObject json = new JsonBuffer("https://dev.tescolabs.com/grocery/products/?query=" + command[1].replace(" ", "%20") + "&offset=0&limit=1", "default", "default", "Ocp-Apim-Subscription-Key", Utils.getApiKey("tesco")).getAsJsonObject();
            JsonObject preData = json.get("uk").getAsJsonObject().get("ghs").getAsJsonObject().get("products").getAsJsonObject();

            if(preData.get("results").getAsJsonArray().size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + command[1] + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = preData.get("results").getAsJsonArray().get(0).getAsJsonObject();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(data.get("name").getAsString())
                    .setThumbnail(data.get("image").getAsString())
                    .setDescription(((data.has("description") && data.get("description").getAsJsonArray().size() > 0) ? data.get("description").getAsJsonArray().get(0).toString() : "No description available."))
                    .addField("Price", "£" + new BigDecimal(data.get("price").getAsDouble()).setScale(2, RoundingMode.HALF_UP) + " (£" + new BigDecimal(data.get("unitprice").getAsDouble()).setScale(2, RoundingMode.HALF_UP) + "/" + data.get("UnitQuantity").getAsString() + ")", true)
                    .addField("Weight", new BigDecimal(data.get("ContentsQuantity").getAsDouble()).setScale(2, RoundingMode.HALF_UP) + data.get("ContentsMeasureType").getAsString(), true)
                    .addField("Quantity", data.get("UnitOfSale").getAsString() + "", true)
                    .addField("Department", data.get("superDepartment").getAsString() + " (" + data.get("department").getAsString() + ")", true)
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName() , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
