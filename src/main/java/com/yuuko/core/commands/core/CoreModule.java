package com.yuuko.core.commands.core;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.core.commands.*;
import com.yuuko.core.commands.setting.commands.SettingsCommand;

import java.util.Map;

import static java.util.Map.entry;

public class CoreModule extends Module {
	private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
			entry("about", AboutCommand.class),
			entry("settings", SettingsCommand.class),
			entry("module", ModuleCommand.class),
			entry("bind", BindCommand.class),
			entry("help", HelpCommand.class),
			entry("shards", ShardsCommand.class),
			entry("vote", VoteCommand.class),
			entry("permissions", PermissionsCommand.class)
	);

	public CoreModule() {
		super("core", false, commands);
	}
}
