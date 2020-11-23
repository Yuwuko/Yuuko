package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
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
        try {
            Config.LOG_METRICS = !Config.LOG_METRICS;
            EmbedBuilder embed = new EmbedBuilder().setTitle("Logging Metrics").setDescription("Now " + Config.LOG_METRICS + ".");
            MessageHandler.reply(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
