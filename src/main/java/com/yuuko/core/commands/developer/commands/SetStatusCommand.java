package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetStatusCommand extends Command {

    public SetStatusCommand() {
        super("setstatus", DeveloperModule.class, 1, new String[]{"-setstatus <type> <status>"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 3);

            switch(commandParameters[0].toLowerCase()) {
                case "playing":
                    e.getJDA().getPresence().setGame(Game.of(Game.GameType.DEFAULT, commandParameters[1]));
                    break;
                case "listening":
                    e.getJDA().getPresence().setGame(Game.of(Game.GameType.LISTENING, commandParameters[1]));
                    break;
                case "streaming":
                    e.getJDA().getPresence().setGame(Game.of(Game.GameType.STREAMING, commandParameters[1]));
                    break;
                case "watching":
                    e.getJDA().getPresence().setGame(Game.of(Game.GameType.WATCHING, commandParameters[1]));
                    break;
                default:
                    e.getJDA().getPresence().setGame(Game.of(Game.GameType.WATCHING, "@Yuuko help"));
            }

            EmbedBuilder embed = new EmbedBuilder().setTitle("Status changed successfully.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
