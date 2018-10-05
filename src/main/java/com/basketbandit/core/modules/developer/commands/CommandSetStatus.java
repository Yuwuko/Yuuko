package com.basketbandit.core.modules.developer.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetStatus extends Command {

    public CommandSetStatus() {
        super("setstatus", "com.basketbandit.core.modules.developer.ModuleDeveloper", 1, new String[]{"-setstatus [type] [status]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 3);

            switch(commandParameters[0]) {
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
                    e.getJDA().getPresence().setGame(Game.of(Game.GameType.WATCHING, "@BasketBandit help"));
            }

            EmbedBuilder embed = new EmbedBuilder().setTitle("Status changed successfully.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
