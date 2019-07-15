package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;

public class SetStatusCommand extends Command {

    public SetStatusCommand() {
        super("setstatus", Configuration.MODULES.get("developer"), 1, Arrays.asList("-setstatus <type> <status>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            String[] commandParameters = e.getParameters().split("\\s+", 3);

            switch(commandParameters[0].toLowerCase()) {
                case "playing":
                    e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, commandParameters[1]));
                    break;
                case "listening":
                    e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, commandParameters[1]));
                    break;
                case "streaming":
                    e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.STREAMING, commandParameters[1]));
                    break;
                case "watching":
                    e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, commandParameters[1]));
                    break;
                default:
                    e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "@Yuuko help"));
            }

            EmbedBuilder embed = new EmbedBuilder().setTitle("Status changed successfully.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
