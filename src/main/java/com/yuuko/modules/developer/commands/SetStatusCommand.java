package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Arrays;

public class SetStatusCommand extends Command {

    public SetStatusCommand() {
        super("setstatus", Arrays.asList("-setstatus <type> <status>"), 2);
    }

    @Override
    public void onCommand(MessageEvent context) {
        String[] params = context.getParameters().split("\\s+", 3);
        switch (params[0].toLowerCase()) {
            case "playing" -> context.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, params[1]));
            case "listening" -> context.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.LISTENING, params[1]));
            case "streaming" -> context.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.STREAMING, params[1]));
            case "watching" -> context.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, params[1]));
            default -> context.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "@Yuuko help"));
        }
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "success"));
        MessageDispatcher.reply(context, embed.build());
    }

}
