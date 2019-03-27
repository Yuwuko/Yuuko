package com.yuuko.core.commands.core;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;

public class CoreModule extends Module {
	private static final Command[] commands = new Command[]{
			new AboutCommand(),
			new SettingsCommand(),
			new ModuleCommand(),
			new HelpCommand(),
			new ShardsCommand(),
			new VoteCommand(),
			new CommandCommand()
	};

	public CoreModule(MessageEvent e) {
		super("Core", null, false, commands);
		new CommandExecutor(e, this);
	}

}
