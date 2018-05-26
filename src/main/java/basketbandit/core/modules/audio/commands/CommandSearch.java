package basketbandit.core.modules.audio.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandSearch extends Command {

    public CommandSearch() {
        super("search", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandSearch(MessageReceivedEvent e) {
        super("search", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        List<SearchResult> results = ModuleAudio.searchYouTubeAux(e);

        if(results != null) {
            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor(e.getAuthor().getName(), null, e.getAuthor().getAvatarUrl())
                    .setDescription("Type in the number of the track you would like to play or cancel.")
                    .addField("", "`1:` " + results.get(0).getSnippet().getTitle(), false)
                    .addField("", "`2:` " + results.get(1).getSnippet().getTitle(), false)
                    .addField("", "`3:` " + results.get(2).getSnippet().getTitle(), false)
                    .addField("", "`4:` " + results.get(3).getSnippet().getTitle(), false)
                    .addField("", "`5:` " + results.get(4).getSnippet().getTitle(), false)
                    .addField("", "`6:` " + results.get(5).getSnippet().getTitle(), false)
                    .addField("", "`7:` " + results.get(6).getSnippet().getTitle(), false)
                    .addField("", "`8:` " + results.get(7).getSnippet().getTitle(), false)
                    .addField("", "`9:` " + results.get(8).getSnippet().getTitle(), false)
                    .addField("", "`10:` " + results.get(9).getSnippet().getTitle(), false)
                    .setFooter("Version: " + Configuration.VERSION, null);

            ModuleAudio.searchUsers.put(e.getAuthor().getIdLong(), results);
            e.getTextChannel().sendMessage(presentResults.build()).queue();
        }
        return true;
    }

}
