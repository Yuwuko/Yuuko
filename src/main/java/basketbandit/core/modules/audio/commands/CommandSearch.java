package basketbandit.core.modules.audio.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.handlers.YouTubeSearchHandler;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandSearch extends Command {

    public CommandSearch() {
        super("search", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandSearch(MessageReceivedEvent e, String[] command) {
        super("search", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {

        List<SearchResult> results = YouTubeSearchHandler.searchList(e, command);
        StringBuilder resultString = new StringBuilder();

        if(results != null) {
            int i = 1;
            for(SearchResult result : results) {
                resultString.append("`").append(i).append(":` ").append(result.getSnippet().getTitle()).append("\n\n");
                i++;
            }
        } else {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", there was a problem processing your request.").queue();
            return;
        }

            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor(e.getAuthor().getName() + ", results for: " + command[1], null, e.getAuthor().getAvatarUrl())
                    .setDescription("Type in the number of the track you would like to play or type cancel to stop me waiting for a response. \n\n" + resultString)
                    .setFooter("Version: " + Configuration.VERSION, null);

            ModuleAudio.searchUsers.put(e.getAuthor().getIdLong(), results);
            e.getTextChannel().sendMessage(presentResults.build()).queue();
    }

}
