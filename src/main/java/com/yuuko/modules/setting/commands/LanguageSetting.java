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
        super("language", Arrays.asList("-language en", "-language fr"), Arrays.asList(Permission.MANAGE_SERVER), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!I18n.getSupportedLanguages().contains(context.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("invalid_lang"))
                    .setDescription(context.i18n("invalid_lang_desc").formatted(Arrays.toString(I18n.getSupportedLanguages().toArray())));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(GuildFunctions.setGuildSettings("language", context.getParameters(), context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("lang_changed"))
                    .setDescription(context.i18n("lang_changed_desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
        }
    }
}
