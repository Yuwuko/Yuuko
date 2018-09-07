package com.basketbandit.core.modules.utility.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CommandServer extends Command {

    public CommandServer() {
        super("server", "com.basketbandit.core.modules.utility.ModuleUtility", new String[]{"-server"}, null);
    }

    public CommandServer(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        Guild server = e.getGuild();

        EmbedBuilder commandInfo = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Server information for: " + server.getName(), null)
                .setThumbnail(server.getIconUrl())
                .addField("Owner", server.getOwner().getUser().getName() + "#" + server.getOwner().getUser().getDiscriminator() + " (" + server.getOwner().getEffectiveName() + ")", true)
                .addField("ID", server.getId(), true)
                .addField("Created", server.getCreationTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mma")), true)
                .addField("Region", server.getRegion().getName(), true)
                .addField("Users", server.getMemberCache().size()+"", true)
                .addField("Text Channels", server.getTextChannelCache().size()+"", true)
                .addField("Voice Channels", server.getVoiceChannels().size()+"", true)
                .addField("Roles", server.getRoles().size()+"", true)
                .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

        Utils.sendMessage(e, commandInfo.build());
    }

}
