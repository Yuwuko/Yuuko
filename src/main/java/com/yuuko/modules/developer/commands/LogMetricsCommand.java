package com.yuuko.modules.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LogMetricsCommand extends Command {

    public LogMetricsCommand() {
        super("logmetrics", 0, -1L, Arrays.asList("-logm"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Yuuko.LOG_METRICS = !Yuuko.LOG_METRICS;
        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e, "desc").formatted(Yuuko.LOG_METRICS));
        MessageDispatcher.reply(e, embed.build());
    }
}
