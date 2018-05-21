package basketbandit.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.tfl.LineManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommandLineStatus extends Command {

    CommandLineStatus() {
        super("linestatus", "transport", null);
    }

    public CommandLineStatus(MessageReceivedEvent e) {
        super("linestatus", "transport", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        try {
            URL url = new URL("https://api.tfl.gov.uk/line/mode/tube/status?app_id=" + Configuration.TFL_ID + "&app_key=" + Configuration.TFL_API);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if(conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            String json;
            try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = conn.getInputStream().read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }

                json = result.toString();
            }

            ArrayList<LineManager> lineManager = new ObjectMapper().readValue(json, new TypeReference<List<LineManager>>(){});

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("Tube Line Status - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")))
                    .addField(lineManager.get(0).getName(),lineManager.get(0).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(1).getName(),lineManager.get(1).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(2).getName(),lineManager.get(2).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(3).getName(),lineManager.get(3).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(4).getName(),lineManager.get(4).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(5).getName(),lineManager.get(5).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(6).getName(),lineManager.get(6).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(7).getName(),lineManager.get(7).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(8).getName(),lineManager.get(8).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(9).getName(),lineManager.get(9).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .addField(lineManager.get(10).getName(),lineManager.get(10).getLineStatuses().get(0).getStatusSeverityDescription(),false)
                    .setFooter("Version: " + Configuration.VERSION + ", Data provided by tfl.gov.uk", e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl())
            ;
            e.getTextChannel().sendMessage(embed.build()).queue();
            return true;

        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
