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

public class RestartCommand extends Command {
    private final Pattern range = Pattern.compile("[0-9]+\\s*-\\s*[0-9]+");
    private final Pattern list = Pattern.compile("([0-9]+\\s*,\\s*)+[0-9]+");

    public RestartCommand() {
        super("restart", Config.MODULES.get("developer"), 0, -1L, Arrays.asList("-restart <shard_id>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            DatabaseFunctions.getShardStatistics().forEach(shard -> {
                DatabaseFunctions.triggerRestartSignal(shard.getId());
            });
            return;
        }

        if(Sanitiser.isNumber(e.getParameters())) {
            DatabaseFunctions.triggerRestartSignal(Integer.parseInt(e.getParameters()));
            EmbedBuilder embed = new EmbedBuilder().setTitle("Restart").setDescription("Attempted to set restart trigger for shard: " + e.getParameters());
            MessageHandler.reply(e, embed.build());
            return;
        }

        if(range.matcher(e.getParameters()).matches()) {
            String[] shards = e.getParameters().split("-");
            for(int i = Integer.parseInt(shards[0]); i <= Integer.parseInt(shards[1]); i++) {
                DatabaseFunctions.triggerShutdownSignal(i);
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle("Restart").setDescription("Attempted to set restart trigger for shards: " + e.getParameters());
            MessageHandler.reply(e, embed.build());
            return;
        }

        if(list.matcher(e.getParameters()).matches()) {
            String[] shards = e.getParameters().split(",");
            for(String shard: shards) {
                DatabaseFunctions.triggerShutdownSignal(Integer.parseInt(shard));
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle("Restart").setDescription("Attempted to set restart trigger for shards: " + e.getParameters());
            MessageHandler.reply(e, embed.build());
        }
    }

}