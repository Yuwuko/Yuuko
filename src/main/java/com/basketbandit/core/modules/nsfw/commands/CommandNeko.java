package com.basketbandit.core.modules.nsfw.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.SystemVariables;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import pw.aru.api.nekos4j.image.Image;
import pw.aru.api.nekos4j.image.ImageProvider;

public class CommandNeko extends Command {

    public CommandNeko() {
        super("neko", "com.basketbandit.core.modules.nsfw.ModuleNSFW", 0, new String[]{"-neko [type]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            ImageProvider imageProvider = SystemVariables.nekoApi.getImageProvider();
            Image image = (command.length > 1) ? imageProvider.getRandomImage(command[1]).execute() : imageProvider.getRandomImage("lewd").execute();

            EmbedBuilder lewdNeko = new EmbedBuilder()
                    .setTitle(image.getUrl())
                    .setImage(image.getUrl())
                    .setFooter(Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            Utils.sendMessage(e, lewdNeko.build());

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
