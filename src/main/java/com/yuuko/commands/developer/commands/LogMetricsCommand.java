package com.yuuko.commands.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LogMetricsCommand extends Command {

    public LogMetricsCommand() {
        super("logmetrics", Yuuko.MODULES.get("developer"), 0, -1L, Arrays.asList("-logm"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Yuuko.LOG_METRICS = !Yuuko.LOG_METRICS;
        EmbedBuilder embed = new EmbedBuilder().setTitle("Metrics Logging").setDescription("Metrics logging has been set to: " + Yuuko.LOG_METRICS);
        MessageDispatcher.reply(e, embed.build());
    }
}
