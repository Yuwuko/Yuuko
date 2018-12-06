package com.yuuko.core.modules.nsfw.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import com.yuuko.core.utils.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandNeko extends Command {

    public CommandNeko() {
        super("neko", "com.yuuko.core.modules.nsfw.ModuleNSFW", 0, new String[]{"-neko", "-neko [type]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String inputString = (command.length > 1) ? command[1] : "lewd";
            String json = new JsonBuffer().getString("https://nekos.life/api/v2/img/" + inputString, "default", "default", null, null);
            String url;

            if(!json.contains("404")) {
                url = json.substring(json.indexOf("h"), json.lastIndexOf("\""));
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No image was found with that parameter! (Go to https://nekos.life/api/v2/endpoints for a list of valid parameters)");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            EmbedBuilder lewdNeko = new EmbedBuilder()
                    .setTitle("Neko: " + url)
                    .setImage(url)
                    .setFooter(Cache.STANDARD_STRINGS[2] + e.getAuthor().getName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, lewdNeko.build());

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
