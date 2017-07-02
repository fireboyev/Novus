package com.fireboyev.discord.novus.commandmanager;

public class Command {
	String name;
	CommandExecutor command;
	CommandDescription desc;

	public Command(String name, CommandExecutor command, CommandDescription desc) {
		this.name = name;
		this.command = command;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public CommandExecutor getExecutor() {
		return command;
	}

	public CommandDescription getDescription() {
		return desc;
	}
}
