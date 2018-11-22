package com.yuuko.core.modules.core;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCore extends Module {

	public ModuleCore(MessageReceivedEvent e, String[] command) {
		super("ModuleCore", null, new Command[]{
				new CommandAbout(),
				new CommandSettings(),
				new CommandModule(),
				new CommandModules(),
				new CommandHelp(),
				new CommandSetup()
		});

		// Core module doesn't have database checks because it cannot be toggled
		// since it is a core part of the bot and has useful modules that aren't
		// to do with moderation and are more about the bot itself.
		if(e != null && command != null) {
			new CommandExecutor(e, command, this);
		}
	}

}
