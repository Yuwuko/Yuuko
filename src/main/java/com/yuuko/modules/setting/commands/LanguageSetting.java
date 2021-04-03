package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class LanguageSetting extends Command {

    public LanguageSetting() {
        super("language", 1, -1L, Arrays.asList("-language en", "-language fr"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!I18n.getSupportedLanguages().contains(context.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Language").setDescription("Only the following languages are fully or partially supported: " + Arrays.toString(I18n.getSupportedLanguages().toArray()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(GuildFunctions.setGuildSettings("language", context.getParameters(), context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Language Changed").setDescription("The language has been successfully changed to `" + context.getParameters() + "`");
            MessageDispatcher.reply(context, embed.build());
        }
    }
}
