package basketbandit.core.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Random;

public class CommandInsult extends Command{

    CommandInsult() {
        super("insult", "basketbandit.core.modules.ModuleFun", null);
    }

    public CommandInsult(MessageReceivedEvent e) {
        super("insult", "basketbandit.core.modules.ModuleFun", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     * @throws NoSuchElementException ;
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        try {
            int lines = 0;
            BufferedReader insults;

            insults = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("insults.txt")));
            while (insults.readLine() != null) lines++;
            insults.close();

            insults = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("insults.txt")));
            for(int i = 0; i < new Random().nextInt(lines); i++) insults.readLine();
            e.getTextChannel().sendMessage(e.getGuild().getMembers().get(new Random().nextInt(e.getGuild().getMembers().size())).getUser().getAsMention() + insults.readLine()).queue();

            return true;

        } catch(Exception ex) {
            e.getTextChannel().sendMessage("Oh... uh, there was an exception while executing the insult command...").queue();
            return false;
        }
    }

}
