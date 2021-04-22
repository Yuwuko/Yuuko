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
        super("shutdown", Arrays.asList("-shutdown"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            System.exit(0);
            return;
        }

        if(Sanitiser.isNumeric(context.getParameters())) {
            ShardFunctions.triggerShutdownSignal(Integer.parseInt(context.getParameters()));
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(range.matcher(context.getParameters()).matches()) {
            String[] shards = context.getParameters().split("-");
            for(int i = Integer.parseInt(shards[0]); i <= Integer.parseInt(shards[1]); i++) {
                ShardFunctions.triggerShutdownSignal(i);
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(list.matcher(context.getParameters()).matches()) {
            String[] shards = context.getParameters().split(",");
            for(String shard: shards) {
                ShardFunctions.triggerShutdownSignal(Integer.parseInt(shard));
            }
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
        }
    }

}
