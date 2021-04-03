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
        super("user", 1, -1L, Arrays.asList("-user @user", "-user <userId>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
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
            roleString.append("None");
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
                .setTitle("Information about **" + target.getEffectiveName() + "**")
                .setDescription("**" + target.getEffectiveName() + "** is currently **" + target.getOnlineStatus().name().toLowerCase() + "** " + presence)
                .setThumbnail(target.getUser().getAvatarUrl())
                .addField("Username", target.getUser().getAsTag(), true)
                .addField("User ID", target.getUser().getId(), true)
                .addField("Account Created", target.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), true)
                .addField("Joined Server", target.getTimeJoined().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), true)
                .addField("Bot?", target.getUser().isBot() + "", true)
                .addField("Roles", roleString.toString(), true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
