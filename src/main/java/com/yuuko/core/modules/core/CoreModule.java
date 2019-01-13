package com.yuuko.core.modules.core;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CoreModule extends Module {

	public CoreModule(MessageReceivedEvent e, String[] command) {
		super("Core", null, false, new Command[]{
				new AboutCommand(),
				new SettingsCommand(),
				new ModuleCommand(),
				new ModulesCommand(),
				new HelpCommand()
		});

		new CommandExecutor(e,this, command);
	}

}
