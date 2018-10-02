package com.basketbandit.core.modules.media.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CommandRuneScape extends Command {

    public CommandRuneScape() {
        super("runescape", "com.basketbandit.core.modules.media.ModuleMedia", 2, new String[]{"-runescape [media] [player]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        String player = commandParameters[1].toLowerCase();
        Boolean osrs = (commandParameters[0].equals("os"));

        try {
            URL url = (!osrs) ? new URL("http://services.runescape.com/m=hiscore/index_lite.ws?player=" + player) : new URL("http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=" + player);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String gameVersion = (osrs) ? "[OSRS]" : "[RS3]";
            StringBuilder statString = new StringBuilder();
            for(String line; (line = reader.readLine()) != null;) {
                statString.append(line);
            }
            String[] skills = statString.toString().split(",");
            StringBuilder messageString = new StringBuilder("```Showing stats for " + player + "! "+ gameVersion +"```" +
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
                messageString.append("\n" + "Summoning       :: ").append(skills[49]).append("\n").append("Dungeoneering   :: ").append(skills[51]).append("\n").append("Divination      :: ").append(skills[53]).append("\n").append("Invention       :: ").append(skills[55]);
            }
            messageString.append("``````Total Level     :: ").append(skills[1]).append("```");

            Utils.sendMessage(e, messageString.toString());
        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
            Utils.sendMessage(e, "Oops, looks like I messed up! (Or that account doesn't exist!) <:ErioTouwa:420413779323650050>");
        }

    }

}
