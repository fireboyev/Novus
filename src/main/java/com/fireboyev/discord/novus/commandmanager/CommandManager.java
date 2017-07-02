package com.fireboyev.discord.novus.commandmanager;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
	List<Command> commands;

	public CommandManager() {
		commands = new ArrayList<Command>();
	}

	public void registerCommand(String name, CommandDescription desc, CommandExecutor command) {
		commands.add(new Command(name, command, desc));
	}

	public List<Command> getCommands() {
		return commands;
	}
}
