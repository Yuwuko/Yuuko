package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LogMetricsCommand extends Command {

    public LogMetricsCommand() {
        super("logmetrics", 0, -1L, Arrays.asList("-logmetrics"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        Yuuko.LOG_METRICS = !Yuuko.LOG_METRICS;
        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc").formatted(Yuuko.LOG_METRICS));
        MessageDispatcher.reply(context, embed.build());
    }
}
