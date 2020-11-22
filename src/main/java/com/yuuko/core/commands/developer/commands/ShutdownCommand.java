package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ShutdownCommand extends Command {
    private final Pattern range = Pattern.compile("[0-9]+\\s*-\\s*[0-9]+");
    private final Pattern list = Pattern.compile("([0-9]+\\s*,\\s*)+[0-9]+");

    public ShutdownCommand() {
        super("shutdown", Config.MODULES.get("developer"), 0, -1L, Arrays.asList("-shutdown"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            System.exit(0);
            return;
        }

        if(Sanitiser.isNumber(e.getParameters())) {
            DatabaseFunctions.triggerShutdownSignal(Integer.parseInt(e.getParameters()));
            EmbedBuilder embed = new EmbedBuilder().setTitle("Shutdown").setDescription("Attempted to set shutdown trigger for shard: " + e.getParameters());
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(range.matcher(e.getParameters()).matches()) {
            String[] shards = e.getParameters().split("-");
            for(int i = Integer.parseInt(shards[0]); i <= Integer.parseInt(shards[1]); i++) {
                DatabaseFunctions.triggerShutdownSignal(i);
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle("Shutdown").setDescription("Attempted to set shutdown trigger for shards: " + e.getParameters());
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(list.matcher(e.getParameters()).matches()) {
            String[] shards = e.getParameters().split(",");
            for(String shard: shards) {
                DatabaseFunctions.triggerShutdownSignal(Integer.parseInt(shard));
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle("Shutdown").setDescription("Attempted to set shutdown trigger for shards: " + e.getParameters());
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
