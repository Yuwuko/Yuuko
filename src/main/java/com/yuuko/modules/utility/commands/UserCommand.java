package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class UserCommand extends Command {

    public UserCommand() {
        super("user", Arrays.asList("-user @user", "-user <userId>"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) {
        Member target = MessageUtilities.getMentionedMember(context, true);

        if(target == null) {
            return;
        }

        // Gets user's roles, replaces the last comma with nothing.
        final StringBuilder roleString = new StringBuilder();
        if(target.getRoles().size() > 0) {
            target.getRoles().forEach(role -> roleString.append(role.getName()).append(", "));
            roleString.replace(roleString.lastIndexOf(", "), roleString.length() - 1, "");
        } else {
            roleString.append(context.i18n("none"));
        }

        String presence = "";
        if(target.getActivities().size() > 0) {
            presence = switch (target.getActivities().get(0).getType().name()) {
                case "LISTENING" -> "and is listening to ";
                case "DEFAULT" -> "and is playing ";
                case "STREAMING" -> "and is streaming ";
                case "WATCHING" -> "and is watching ";
                default -> "";
            };

            if(!presence.equals("")) {
                presence += (target.getActivities().get(0).isRich()) ? "**"
                        + target.getActivities().get(0).getName()
                        + "** ~ **"
                        + target.getActivities().get(0).asRichPresence().getState()
                        + "** - **"
                        + target.getActivities().get(0).asRichPresence().getDetails()
                        + "**" : "**"
                        + target.getActivities().get(0).getName()
                        + "**";
            }
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n("title").formatted(target.getEffectiveName()))
                .setDescription(context.i18n("desc").formatted(target.getEffectiveName(), target.getOnlineStatus().name().toLowerCase(), presence))
                .setThumbnail(target.getUser().getAvatarUrl())
                .addField(context.i18n("user"), target.getUser().getAsTag(), true)
                .addField(context.i18n("user_id"), target.getUser().getId(), true)
                .addField(context.i18n("created"), target.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), true)
                .addField(context.i18n("joined"), target.getTimeJoined().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), true)
                .addField(context.i18n("bot"), target.getUser().isBot() + "", true)
                .addField(context.i18n("roles"), roleString.toString(), true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
