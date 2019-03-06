package com.yuuko.core.commands.core;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CoreModule extends Module {

	public CoreModule(MessageReceivedEvent e, String[] command) {
		super("Core", null, false, new Command[]{
				new AboutCommand(),
				new SettingsCommand(),
				new ModuleCommand(),
				new HelpCommand(),
				new ShardsCommand(),
				new VoteCommand(),
				new CommandCommand()
		});

		new CommandExecutor(e,this, command);
	}

}
