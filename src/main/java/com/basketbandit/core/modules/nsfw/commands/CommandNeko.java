package com.basketbandit.core.modules.nsfw.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import com.basketbandit.core.utils.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandNeko extends Command {

    public CommandNeko() {
        super("neko", "com.basketbandit.core.modules.nsfw.ModuleNSFW", 0, new String[]{"-neko [type]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String inputString = (command.length > 1) ? command[1] : "lewd";
            String json = new JsonBuffer().getString("https://nekos.life/api/v2/img/" + inputString, "default", "default");
            String url;

            if(!json.contains("404")) {
                url = json.substring(12, json.lastIndexOf("\""));
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No image was found with that parameter! (Go to https://nekos.life/api/v2/endpoints for a list of valid parameters)");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            EmbedBuilder lewdNeko = new EmbedBuilder()
                    .setTitle("Neko: " + url)
                    .setImage(url)
                    .setFooter(Configuration.VERSION + " ·  Requested by " + e.getAuthor().getName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, lewdNeko.build());

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
