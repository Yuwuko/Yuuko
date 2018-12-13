package com.yuuko.core.modules.core;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCore extends Module {

	public ModuleCore(MessageReceivedEvent e, String[] command) {
		super("Core", null, false, new Command[]{
				new CommandAbout(),
				new CommandSettings(),
				new CommandModule(),
				new CommandModules(),
				new CommandHelp(),
				new CommandSetup()
		});

		new CommandExecutor(e, command, this);
	}

}
