package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.regex.Pattern;

public class ShutdownCommand extends Command {
    private final Pattern range = Pattern.compile("[0-9]+\\s*-\\s*[0-9]+");
    private final Pattern list = Pattern.compile("([0-9]+\\s*,\\s*)+[0-9]+");

    public ShutdownCommand() {
        super("shutdown", 0, -1L, Arrays.asList("-shutdown"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            System.exit(0);
            return;
        }

        if(Sanitiser.isNumeric(e.getParameters())) {
            ShardFunctions.triggerShutdownSignal(Integer.parseInt(e.getParameters()));
            EmbedBuilder embed = new EmbedBuilder().setTitle("Shutdown").setDescription("Attempted to set shutdown trigger for shard: " + e.getParameters());
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        if(range.matcher(e.getParameters()).matches()) {
            String[] shards = e.getParameters().split("-");
            for(int i = Integer.parseInt(shards[0]); i <= Integer.parseInt(shards[1]); i++) {
                ShardFunctions.triggerShutdownSignal(i);
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle("Shutdown").setDescription("Attempted to set shutdown trigger for shards: " + e.getParameters());
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        if(list.matcher(e.getParameters()).matches()) {
            String[] shards = e.getParameters().split(",");
            for(String shard: shards) {
                ShardFunctions.triggerShutdownSignal(Integer.parseInt(shard));
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle("Shutdown").setDescription("Attempted to set shutdown trigger for shards: " + e.getParameters());
            MessageDispatcher.reply(e, embed.build());
        }
    }

}
