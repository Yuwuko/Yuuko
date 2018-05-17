// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class ModuleFun {

    private MessageReceivedEvent e;

    public ModuleFun(MessageReceivedEvent e) {
        this.e = e;
        Message message = e.getMessage();
        String[] command = message.getContentRaw().split("\\s+", 2);

        if(command[0].equals(Configuration.PREFIX + "overreact")) {
            commandOverreact();
        } else if(command[0].equals(Configuration.PREFIX + "insult")) {
            commandInsult();
        }
    }

    /**
     * Overreact command adds 20 (max number) reacts to the previous message.
     */
    private void commandOverreact() {
        // List of the last 10 messages sent to the channel.
        List<Message> messageList = e.getTextChannel().getHistory().retrievePast(10).complete();

        int i = 0;
        for(Emote emote : e.getGuild().getEmotes()) {
            if(i < 20) {
                messageList.get(0).addReaction(emote).queue();
                i++;
            }
        }
    }

    /**
     * Insult command will insult the author of the previous message. (If that person isn't this bot!)
     */
    private void commandInsult() {
        try {
            BufferedReader insults = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("insults.txt")));
            int lines = 0;

            while (insults.readLine() != null) lines++;

            insults.close();
            insults = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("insults.txt")));

            for(int i = 0; i < new Random().nextInt(lines); i++) insults.readLine();

            e.getTextChannel().sendMessage(e.getGuild().getMembers().get(new Random().nextInt(e.getGuild().getMembers().size())).getUser().getAsMention() + insults.readLine()).queue();

        } catch(Exception r) {
            e.getTextChannel().sendMessage("Oh... uh, there was an exception while executing the insult command...").queue();
        }

    }
}
