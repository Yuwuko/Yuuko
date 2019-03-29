package com.yuuko.core.commands.world.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

public class CountdownCommand extends Command {

    private static final Logger log = LoggerFactory.getLogger(CountdownCommand.class);

    private static final HashMap<String, Date> dates = new HashMap<>(){{
        try {
            // Static Dates
            put("christmas", Date.from(Instant.now()).before(new SimpleDateFormat("dd/MM/yyyy").parse("25/12/" + LocalDateTime.now().getYear())) ? new SimpleDateFormat("dd/MM/yyyy").parse("25/12/" + LocalDateTime.now().getYear()) : new SimpleDateFormat("dd/MM/yyyy").parse("25/12/" + (LocalDateTime.now().getYear() + 1)));
            put("halloween", Date.from(Instant.now()).before(new SimpleDateFormat("dd/MM/yyyy").parse("31/10/" + LocalDateTime.now().getYear())) ? new SimpleDateFormat("dd/MM/yyyy").parse("31/10/" + LocalDateTime.now().getYear()) : new SimpleDateFormat("dd/MM/yyyy").parse("31/10/" + (LocalDateTime.now().getYear() + 1)));
            put("newyear", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("31/12/" + LocalDateTime.now().getYear() + " 23:59:59"));
            // Special Dates
            put("brexit", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/04/2019 23:00:00"));
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", CountdownCommand.class.getSimpleName(), ex.getMessage(), ex);
        }
    }};

    public CountdownCommand() {
        super("countdown", WorldModule.class, 1, new String[]{"-countdown <date>", "-countdown <event>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            if(dates.containsKey(e.getCommand()[1])) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Time Until " + e.getCommand()[1].toUpperCase())
                        .setDescription(TextUtilities.getTimestampVerbose(dates.get(e.getCommand()[1]).toInstant().toEpochMilli() - Instant.now().toEpochMilli()))
                        .setTimestamp(Instant.now())
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            } else {
                if(!Sanitiser.isDate(e.getCommand()[1])) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The date that was input is invalid, required format is `dd/MM/yyyy`.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Time Until " + e.getCommand()[1])
                        .setDescription(TextUtilities.getTimestampVerbose(new SimpleDateFormat("dd/MM/yyyy").parse(e.getCommand()[1]).toInstant().toEpochMilli() - Instant.now().toEpochMilli()))
                        .setTimestamp(Instant.now())
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", CountdownCommand.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
