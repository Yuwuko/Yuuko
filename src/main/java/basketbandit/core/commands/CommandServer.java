package basketbandit.core.commands;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class CommandServer extends Command {

    CommandServer() {
        super("server", "utility", null);
    }

    public CommandServer(MessageReceivedEvent e) {
        super("server", "utility", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        Guild server = e.getGuild();

        EmbedBuilder commandInfo = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Server information for: " + server.getName(), null)
                .setThumbnail(server.getIconUrl())
                .setDescription(
                        "Server Owner                 ::  " + server.getOwner().getUser().getName() + "#" + server.getOwner().getUser().getDiscriminator() + " (" + server.getOwner().getEffectiveName() + ")" + "\n" +
                        "Server ID                         ::  " + server.getIdLong() + "\n" +
                        "Server Created               ::  " + server.getCreationTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mma")) + "\n" +
                        "Server Region                 ::  " + server.getRegion().getName() + "\n" +
                        "Total Users                     ::  " + server.getMembers().size() + "\n" +
                        "Total Text Channels     \u200a::  " + server.getTextChannels().size() + "\n" +
                        "Total Voice Channels   ::  " + server.getVoiceChannels().size() + "\n" +
                        "Total Roles                      ::  " + server.getRoles().size() + "\n"
                )
                .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

        e.getTextChannel().sendMessage(commandInfo.build()).queue();
        return true;
    }

}
