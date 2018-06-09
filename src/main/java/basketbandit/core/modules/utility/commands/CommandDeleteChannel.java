package basketbandit.core.modules.utility.commands;

import basketbandit.core.Utils;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandDeleteChannel extends Command {

    public CommandDeleteChannel() {
        super("delchannel", "basketbandit.core.modules.utility.ModuleUtility", new String[]{"-delchannel", "-delchannel [type] [channel]"}, Permission.MANAGE_CHANNEL);
    }

    public CommandDeleteChannel(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command.length > 1) {
            String[] commandParameters = command[1].split("\\s+", 2);
            String type = commandParameters[0];

            if(type.equals("text")) {
                if(e.getGuild().getTextChannelsByName(commandParameters[1],true).size() == 0) {
                    Utils.sendMessage(e, "Sorry, that text-channel could not be found.");
                    return;
                }
                e.getGuild().getTextChannelsByName(commandParameters[1],true).get(0).delete().queue();
            } else if(type.equals("voice") && commandParameters[1].length() == 18 && Long.parseLong(commandParameters[1]) > 0) {
                if(e.getGuild().getVoiceChannelsByName(commandParameters[1],true).size() == 0) {
                    Utils.sendMessage(e, "Sorry, that voice-channel could not be found.");
                    return;
                }
                e.getGuild().getVoiceChannelsByName(commandParameters[1],true).get(0).delete().queue();
            }
        } else {
            e.getTextChannel().delete().queue();
        }
    }

}
