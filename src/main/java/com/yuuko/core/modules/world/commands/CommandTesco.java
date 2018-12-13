package com.yuuko.core.modules.world.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.world.tesco.Result;
import com.yuuko.core.modules.world.tesco.TescoObject;
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
            String json = new JsonBuffer().getString("https://dev.tescolabs.com/grocery/products/?query=" + command[1].replace(" ", "%20") + "&offset=0&limit=1", "default", "default", "Ocp-Apim-Subscription-Key", Utils.getApiKey("tesco"));
            TescoObject tesco = new ObjectMapper().readValue(json, new TypeReference<TescoObject>(){});

            if(tesco.getUk().getGhs().getProducts().getResults().size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Sorry, **_" + command[1] + "_** returned no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            Result product = tesco.getUk().getGhs().getProducts().getResults().get(0);

            String description = (product.getDescription() != null) ? product.getDescription().get(0) : "No description available.";

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(product.getName())
                    .setThumbnail(product.getImage())
                    .setDescription(description)
                    .addField("Price", "£" + new BigDecimal(product.getPrice()).setScale(2, RoundingMode.HALF_UP) + " (£" + new BigDecimal(product.getUnitprice()).setScale(2, RoundingMode.HALF_UP) + "/" + product.getUnitQuantity() + ")", true)
                    .addField("Weight", new BigDecimal(product.getContentsQuantity()).setScale(2, RoundingMode.HALF_UP) + product.getContentsMeasureType(), true)
                    .addField("Quantity", product.getUnitOfSale() + "", true)
                    .addField("Department", product.getSuperDepartment() + " (" + product.getDepartment() + ")", true)
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName() , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
