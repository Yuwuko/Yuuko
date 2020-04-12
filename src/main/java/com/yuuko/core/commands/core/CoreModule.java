package com.yuuko.core.commands.core;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.commands.*;
import com.yuuko.core.commands.setting.commands.SettingsCommand;

import java.util.Arrays;
import java.util.List;

public class CoreModule extends Module {
	private static final List<Command> commands = Arrays.asList(
			new AboutCommand(),
			new SettingsCommand(),
			new ModuleCommand(),
			new HelpCommand(),
			new ShardsCommand(),
			new VoteCommand(),
			new CommandCommand(),
			new PermissionsCommand()
	);

	public CoreModule() {
		super("core", false, commands);
	}

}
