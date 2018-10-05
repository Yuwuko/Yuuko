package com.basketbandit.core.modules.audio;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.audio.commands.*;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static net.dv8tion.jda.core.audio.hooks.ConnectionStatus.NOT_CONNECTED;

public class ModuleAudio extends Module {

    public ModuleAudio(MessageReceivedEvent e, String[] command) {
        super("ModuleAudio", "moduleAudio", new Command[]{
                new CommandPlay(),
                new CommandPause(),
                new CommandStop(),
                new CommandSearch(),
                new CommandSkip(),
                new CommandClear(),
                new CommandQueue(),
                new CommandBackground(),
                new CommandCurrent(),
                new CommandLast(),
                new CommandShuffle(),
                new CommandRepeat()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                if(!e.getMember().getVoiceState().inVoiceChannel()) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("This command can only be used while in a voice channel.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }
                if(e.getGuild().getAudioManager().getConnectionStatus() == NOT_CONNECTED && !command[0].equals("play") && !command[0].equals("search") && !command[0].equals("background")) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("There is no active audio connection.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }
                new CommandExecutor(e, command, this);
            }
        }

    }

}
