package com.yuuko.modules.media.commands;

import com.baseketbandit.runeapi.RuneAPI;
import com.baseketbandit.runeapi.entity.Skill;
import com.baseketbandit.runeapi.entity.Skills;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.Map;

public class RuneScapeCommand extends Command {

    public RuneScapeCommand() {
        super("osrs", 1, -1L, Arrays.asList("-osrs <user>", "-osrs <user>, <skill>"), false, null, true);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String[] params = e.getParameters().split("\\s*,\\s*");
        Map<String, Skill> skills = RuneAPI.getStats(params[0]);
        if(skills == null || skills.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Unable to retrieve stats for that user, are you sure the username is correct?");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        if(params.length == 1) {
            // added fields manually to mimic in-game ordering
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(e.getParameters())
                    .addField("```    Attack    ```", skills.get("Attack").getLevel() + "", true)
                    .addField("```  Hitpoints   ```", skills.get("Hitpoints").getLevel() + "", true)
                    .addField("```    Mining    ```", skills.get("Mining").getLevel() + "", true)
                    .addField("```   Strength   ```", skills.get("Strength").getLevel() + "", true)
                    .addField("```   Agility    ```", skills.get("Agility").getLevel() + "", true)
                    .addField("```   Smithing   ```", skills.get("Smithing").getLevel() + "", true)
                    .addField("```   Defence    ```", skills.get("Defence").getLevel() + "", true)
                    .addField("```   Herblore   ```", skills.get("Herblore").getLevel() + "", true)
                    .addField("```   Fishing    ```", skills.get("Fishing").getLevel() + "", true)
                    .addField("```    Ranged    ```", skills.get("Ranged").getLevel() + "", true)
                    .addField("```   Thieving   ```", skills.get("Thieving").getLevel() + "", true)
                    .addField("```   Cooking    ```", skills.get("Cooking").getLevel() + "", true)
                    .addField("```    Prayer    ```", skills.get("Prayer").getLevel() + "", true)
                    .addField("```   Crafting   ```", skills.get("Crafting").getLevel() + "", true)
                    .addField("```  Firemaking  ```", skills.get("Firemaking").getLevel() + "", true)
                    .addField("```    Magic     ```", skills.get("Magic").getLevel() + "", true)
                    .addField("```  Fletching   ```", skills.get("Fletching").getLevel() + "", true)
                    .addField("``` Woodcutting  ```", skills.get("Woodcutting").getLevel() + "", true)
                    .addField("```  Runecraft   ```", skills.get("Runecraft").getLevel() + "", true)
                    .addField("```    Slayer    ```", skills.get("Slayer").getLevel() + "", true)
                    .addField("```   Farming    ```", skills.get("Farming").getLevel() + "", true)
                    .addField("``` Construction ```", skills.get("Construction").getLevel() + "", true)
                    .addField("```    Hunter    ```", skills.get("Hunter").getLevel() + "", true)
                    .addField("```   Overall    ```", skills.get("Overall").getLevel() + "", true);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        Skills.asList.forEach(s -> {
            if(s.equalsIgnoreCase(params[1])) {
                Skill skill = skills.get(s);
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(params[0] + "'s " + skill.getName() + " level is " + skill.getLevel() + ".")
                        .addField("```    Rank    ```", TextUtilities.formatInteger(skill.getRank()) + "", true)
                        .addField("``` Experience ```", TextUtilities.formatInteger(skill.getExperience()) + "", true);
                MessageDispatcher.reply(e, embed.build());
            }
        });
    }
}
