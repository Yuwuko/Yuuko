package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class LogMetricsCommand extends Command {

    public LogMetricsCommand() {
        super("logm", Configuration.MODULES.get("developer"), 0, Arrays.asList("-logm"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            Configuration.LOG_METRICS = !Configuration.LOG_METRICS;
            EmbedBuilder embed = new EmbedBuilder().setTitle("Logging Metrics").setDescription("Now " + Configuration.LOG_METRICS + ".");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
