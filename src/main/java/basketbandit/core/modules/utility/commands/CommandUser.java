package basketbandit.core.modules.utility.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

public class CommandUser extends Command {

    public CommandUser() {
        super("user", "basketbandit.core.modules.utility.ModuleUtility", null);
    }

    public CommandUser(MessageReceivedEvent e) {
        super("user", "basketbandit.core.modules.utility.ModuleUtility", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException;
     */
    protected boolean executeCommand(MessageReceivedEvent e) throws NoSuchElementException {
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);
        Member member = null;
        EmbedBuilder commandInfo;
        String userString = command[1].toLowerCase();
        String[] userParts = command[1].split("#", 1);

        // Checks to see if the user is imputing a username or a nickname.
        if(userString.contains("#")) {
            for(Member user : e.getGuild().getMembers()) {
                if(user.getUser().getName().toLowerCase().equals(userParts[0]) && user.getUser().getDiscriminator().equals(command[1])) {
                    member = user;
                    break;
                }
            }
        } else {
            for(Member user : e.getGuild().getMembers()) {
                if(user.getEffectiveName().toLowerCase().equals(command[1].toLowerCase()) || user.getEffectiveName().toLowerCase().contains(command[1].toLowerCase())) {
                    member = user;
                    break;
                }
            }
        }

        if(member == null) {
            e.getTextChannel().sendMessage("Sorry... I couldn't find that user. <:SagiriIzumi:420417275359657986>").queue();
            throw new NoSuchElementException();
        }

        // Gets user's roles, replaces the last comma with nothing.
        List<Role> infoRoles = member.getRoles();
        StringBuilder roleString = new StringBuilder();

        for(Role role : infoRoles) {
            roleString.append(role.getName()).append(", ");
        }

        if(!roleString.toString().equals("")) {
            int index = roleString.lastIndexOf(", ");
            roleString = new StringBuilder(new StringBuilder(roleString.toString()).replace(index, index + 1, "").toString());
        }

        commandInfo = new EmbedBuilder()
                .setColor(Color.RED)
                .setAuthor("User information about " + member.getEffectiveName() + ",", null, member.getUser().getAvatarUrl())
                .setTitle("User is currently "+member.getOnlineStatus())
                .setThumbnail(member.getUser().getAvatarUrl())
                .setDescription(
                        "Username                ::  " + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n" +
                        "UserID                      ::  " + member.getUser().getIdLong() + "\n" +
                        "Account Created   \u200a\u200a::  " + member.getUser().getCreationTime().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")) + "\n" +
                        "Joined Server          \u200a\u200a::  " + member.getJoinDate().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")) + "\n" +
                        "Mutual Servers       ::  " + member.getUser().getMutualGuilds().size() + "\n" +
                        "Roles                         ::  " + roleString + "\n"
                    )
                    .setFooter("Version: " + Configuration.VERSION + ", Information requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

        e.getTextChannel().sendMessage(commandInfo.build()).queue();
        return true;
    }
}
