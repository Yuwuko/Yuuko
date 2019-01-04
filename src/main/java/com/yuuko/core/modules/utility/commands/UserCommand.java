package com.yuuko.core.modules.utility.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.utility.UtilityModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.format.DateTimeFormatter;

public class UserCommand extends Command {

    public UserCommand() {
        super("user", UtilityModule.class, 1, new String[]{"-user @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtility.getFirstMentionedMember(e);

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
        if(target.getGame() != null) {
            switch(target.getGame().getType().name()) {
                case "LISTENING": presence = "and is listening to ";
                break;
                case "DEFAULT": presence = "and is playing ";
                break;
                case "STREAMING": presence = "and is streaming ";
                break;
                case "WATCHING": presence = "and is watching ";
                break;
                default: presence = "";
            }

            if(!presence.equals("")) {
                presence += (target.getGame().isRich()) ? "**" + target.getGame().getName() + "** ~ **" + target.getGame().asRichPresence().getState() + "** - **" + target.getGame().asRichPresence().getDetails() + "**" : "**" + target.getGame().getName() + "**";
            }
        }

        EmbedBuilder commandInfo = new EmbedBuilder()
                .setTitle("Information about **" + target.getEffectiveName() + "**")
                .setDescription("**" + target.getEffectiveName() + "** is currently **" + target.getOnlineStatus().name().toLowerCase() + "** " + presence)
                .setThumbnail(target.getUser().getAvatarUrl())
                .addField("Username", Utils.getTag(target), true)
                .addField("User ID", target.getUser().getId(), true)
                .addField("Account Created", target.getUser().getCreationTime().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), true)
                .addField("Joined Server", target.getJoinDate().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), true)
                .addField("Bot?", target.getUser().isBot() + "", true)
                .addField("Roles", roleString.toString(), true)
                .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
        MessageHandler.sendMessage(e, commandInfo.build());
    }

}
