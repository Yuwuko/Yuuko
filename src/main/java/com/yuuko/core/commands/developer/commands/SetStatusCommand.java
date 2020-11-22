package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;

public class SetStatusCommand extends Command {

    public SetStatusCommand() {
        super("setstatus", Config.MODULES.get("developer"), 1, -1L, Arrays.asList("-setstatus <type> <status>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] params = e.getParameters().split("\\s+", 3);
        switch (params[0].toLowerCase()) {
            case "playing" -> e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, params[1]));
            case "listening" -> e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, params[1]));
            case "streaming" -> e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.STREAMING, params[1]));
            case "watching" -> e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, params[1]));
            default -> e.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "@Yuuko help"));
        }
        EmbedBuilder embed = new EmbedBuilder().setTitle("Status changed successfully.");
        MessageHandler.sendMessage(e, embed.build());
    }

}
