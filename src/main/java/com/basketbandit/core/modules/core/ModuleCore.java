package com.basketbandit.core.modules.core;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.core.commands.*;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleCore extends Module {

	public ModuleCore() {
		super("ModuleCore", null);
	}

	public ModuleCore(MessageReceivedEvent e, String[] command) {
		super("ModuleCore", null);

		// Core module doesn't have database checks because it cannot be toggled
		// since it is a core part of the bot and has useful modules that aren't
		// to do with moderation and are more about the bot itself.
		executeCommand(e, command);
	}

	@Override
	protected void executeCommand(MessageReceivedEvent e, String[] command) {

		if(command[0].equals(C.SETTINGS.getCommandName()) && (e.getMember().hasPermission(C.SETTINGS.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.SETTINGS.getCommandPermission()))) {
			new CommandSettings(e, command);
			return;
		}

		if(command[0].equals(C.MODULE.getCommandName()) && (e.getMember().hasPermission(C.MODULE.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.MODULE.getCommandPermission()))) {
			new CommandModule(e, command);
			return;
		}

		if(command[0].equals(C.SET_PREFIX.getCommandName()) && (e.getMember().hasPermission(C.SET_PREFIX.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.SET_PREFIX.getCommandPermission()))) {
			new CommandSetPrefix(e, command);
			return;
		}

		if(command[0].equals(C.ABOUT.getCommandName())) {
			new CommandAbout(e, command);
			return;
		}

		if(command[0].equals(C.SETUP.getCommandName()) && (e.getMember().hasPermission(C.SETUP.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.SETUP.getCommandPermission()))) {
			new CommandSetup(e, command);
			return;
		}

		if(command[0].equals(C.MODULES.getCommandName())) {
			new CommandModules(e, command);
			return;
		}

		if(command[0].equals(C.HELP.getCommandName())) {
			new CommandHelp(e, command);
			return;
		}

		Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
	}

}
