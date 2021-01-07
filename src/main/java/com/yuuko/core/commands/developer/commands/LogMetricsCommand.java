package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LogMetricsCommand extends Command {

    public LogMetricsCommand() {
        super("logmetrics", Config.MODULES.get("developer"), 0, -1L, Arrays.asList("-logm"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Config.LOG_METRICS = !Config.LOG_METRICS;
        EmbedBuilder embed = new EmbedBuilder().setTitle("Metrics Logging").setDescription("Metrics logging has been set to: " + Config.LOG_METRICS);
        MessageDispatcher.reply(e, embed.build());
    }
}
