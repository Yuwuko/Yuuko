package com.basketbandit.core.modules.game.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.game.wow.Character;
import com.basketbandit.core.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommandWorldOfWarcraftCharacter extends Command {

    public CommandWorldOfWarcraftCharacter() {
        super("wowcharacter", "com.basketbandit.core.modules.game.ModuleGame", new String[]{"-wowcharacter [realm] [name]"}, null);
    }

    public CommandWorldOfWarcraftCharacter(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);

            // Buffers JSON from the given URL and the uses ObjectMapper to turn it into usable Java objects.
            String json = Utils.bufferJson("https://eu.api.battle.net/wow/character/" + commandParameters[0] + "/" + commandParameters[1] + "?fields=titles%2C+guild&locale=en_GB&apikey=" + Configuration.WOW_API);
            ArrayList<Character> chars = new ObjectMapper().readValue(json, new TypeReference<List<Character>>(){});
            Character character = chars.get(0);

            String classString;
            String raceString;
            String factionString;
            String genderString;

            switch(character.get_class()) {
                case 1: classString = "Warrior";
                    break;
                case 2: classString = "Paladin";
                    break;
                case 3: classString = "Hunter";
                    break;
                case 4: classString = "Rogue";
                    break;
                case 5: classString = "Priest";
                    break;
                case 6: classString = "Death Knight";
                    break;
                case 7: classString = "Shaman";
                    break;
                case 8: classString = "Mage";
                    break;
                case 9: classString = "Warlock";
                    break;
                case 10: classString = "Monk";
                    break;
                case 11: classString = "Druid";
                    break;
                case 12: classString = "Demon Hunter";
                    break;
                default:classString = "Unknown";
            }

            switch(character.getRace()) {
                case 1: raceString = "Human";
                    break;
                case 2: raceString = "Orc";
                    break;
                case 3: raceString = "Dwarf";
                    break;
                case 4: raceString = "Night Elf";
                    break;
                case 5: raceString = "Undead";
                    break;
                case 6: raceString = "Tauren";
                    break;
                case 7: raceString = "Gnome";
                    break;
                case 8: raceString = "Troll";
                    break;
                case 9: raceString = "Goblin";
                    break;
                case 10: raceString = "Blood Elf";
                    break;
                case 11: raceString = "Draenei";
                    break;
                case 22: raceString = "Worgen";
                    break;
                case 24: raceString = "Pandaren";
                    break;
                case 25: raceString = "Pandaren";
                    break;
                case 26: raceString = "Pandaren";
                    break;
                case 27: raceString = "Nightborne";
                    break;
                case 28: raceString = "Highmountain Tauren";
                    break;
                case 29: raceString = "Void Elf";
                    break;
                case 30: raceString = "Lightforged Draenei";
                    break;
                case 34: raceString = "Dark Iron Dwarf";
                    break;
                case 36: raceString = "Mag'har Orc";
                    break;
                default: raceString = "Unknown";
            }

            switch(character.getFaction()) {
                case 0: factionString = "Alliance";
                    break;
                case 1: factionString = "Horde";
                    break;
                default: factionString = "Unknown";
            }

            switch(character.getGender()) {
                case 0: genderString = "Male";
                    break;
                case 1: genderString = "Female";
                    break;
                default: genderString = "Unknown";
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("World of Warcraft - Character information for " + character.getName())
                    .setThumbnail("https://render-eu.worldofwarcraft.com/character/" + character.getThumbnail())
                    .addField("Realm", character.getRealm(), true)
                    .addField("Level", character.getLevel()+"", true)
                    .addField("Class", classString, true)
                    .addField("Race", raceString, true)
                    .addField("Gender", genderString, true)
                    .addField("Faction", factionString, true)
                    .addField("Achievement Points", character.getAchievementPoints()+"", true)
                    .addField("Guild", character.getGuild().getName(), true)
                    .addField("Battlegroup", character.getBattlegroup(), true)
                    .addField("Honorable Kills", character.getTotalHonorableKills()+"", true)
                    .setFooter("Version: " + Configuration.VERSION + ", Data provided by Blizzard" , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            Utils.sendMessage(e, embed.build());

        } catch(Exception ex) {
            Utils.sendMessage(e, "There was an issue processing your request.");
            ex.printStackTrace();
        }

    }

}
