package basketbandit.core.modules.core;

import basketbandit.core.Utils;
import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.core.commands.*;
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

	protected void executeCommand(MessageReceivedEvent e, String[] command) {
		if(command[0].equals(C.SETUP.getCommandName()) && e.getMember().hasPermission(C.SETUP.getCommandPermission())) {
			new CommandSetup(e, command);
			return;
		}

		if(command[0].equals(C.MODULE.getCommandName()) && e.getMember().hasPermission(C.MODULE.getCommandPermission())) {
			new CommandModule(e, command);
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

		if(command[0].equals(C.ABOUT.getCommandName())) {
			new CommandAbout(e, command);
			return;
		}

		if(command[0].equals(C.SET_PREFIX.getCommandName()) && e.getMember().hasPermission(C.SET_PREFIX.getCommandPermission())) {
			new CommandSetPrefix(e, command);
			return;
		}

		Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
	}

}
