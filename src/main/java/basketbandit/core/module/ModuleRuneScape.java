// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

class ModuleRuneScape {

    private MessageReceivedEvent e;

    ModuleRuneScape(MessageReceivedEvent e) {
        this.e = e;
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);

        commandRsstats(command);
    }

    /**
     * RuneScape stats command returns given players RuneScape stats.
     * @param command the original command.
     */
    private void commandRsstats(String[] command) {

        Boolean osrs = (command[0].toLowerCase().equals(Configuration.PREFIX + "osstats"));

        try {
            URL url = (!osrs) ? new URL("http://services.runescape.com/m=hiscore/index_lite.ws?player=" + command[1]) : new URL("http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=" + command[1]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String gameVersion = (osrs) ? "[OSRS]" : "[RS3]";
            StringBuilder statString = new StringBuilder();
            for(String line; (line = reader.readLine()) != null;) {
                statString.append(line);
            }
            String[] skills = statString.toString().split(",");
            StringBuilder messageString = new StringBuilder("```Showing stats for " + command[1] + "! "+ gameVersion +"```" +
                    "```Attack          :: " + skills[3] + "\n"
                    + "Defense         :: " + skills[5] + "\n"
                    + "Strength        :: " + skills[7] + "\n"
                    + "Constitution    :: " + skills[9] + "\n"
                    + "Ranged          :: " + skills[11] + "\n"
                    + "Prayer          :: " + skills[13] + "\n"
                    + "Magic           :: " + skills[15] + "\n"
                    + "Cooking         :: " + skills[17] + "\n"
                    + "Woodcutting     :: " + skills[19] + "\n"
                    + "Fletching       :: " + skills[21] + "\n"
                    + "Fishing         :: " + skills[23] + "\n"
                    + "Firemaking      :: " + skills[25] + "\n"
                    + "Crafting        :: " + skills[27] + "\n"
                    + "Smithing        :: " + skills[29] + "\n"
                    + "Mining          :: " + skills[31] + "\n"
                    + "Herblore        :: " + skills[33] + "\n"
                    + "Agility         :: " + skills[35] + "\n"
                    + "Thieving        :: " + skills[37] + "\n"
                    + "Slayer          :: " + skills[39] + "\n"
                    + "Farming         :: " + skills[41] + "\n"
                    + "Runecrafting    :: " + skills[43] + "\n"
                    + "Hunter          :: " + skills[45] + "\n"
                    + "Construction    :: " + skills[47] + "");
            if(!osrs) {
                messageString.append("\n"+"Summoning       :: " + skills[49] + "\n"
                        + "Dungeoneering   :: " + skills[51] + "\n"
                        + "Divination      :: " + skills[53] + "\n"
                        + "Invention       :: " + skills[55]);
            }
            messageString.append("``````Total Level     :: " + skills[1] + "```");

            e.getTextChannel().sendMessage(messageString).queue();
        } catch(Exception r) {
            e.getTextChannel().sendMessage("Oops, looks like I messed up! (Or that account doesn't exist!) <:ErioTouwa:420413779323650050>").queue();
        }
    }

}
