package com.fireboyev.discord.novus.commandmanager;

public class CommandDescription {
	public String name;
	public String discription;
	public Boolean isVisible;
	public String[] aliases;
	public String usage;

	public CommandDescription(String name, String discription, Boolean isVisible, String[] aliases, String usage) {
		this.name = name;
		this.discription = discription;
		this.isVisible = isVisible;
		this.aliases = aliases;
		this.usage = usage;
	}

	public CommandDescription(String name, String discription, String[] aliases, String usage) {
		this.name = name;
		this.discription = discription;
		this.isVisible = true;
		this.aliases = aliases;
		this.usage = usage;
	}

	public CommandDescription(String name, String discription, String usage) {
		this.name = name;
		this.discription = discription;
		this.isVisible = true;
		this.aliases = new String[] {};
		this.usage = usage;
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public String getDescription() {
		return discription;
	}

	public Boolean isVisible() {
		return isVisible;
	}
	public String getUsage(){
		return usage;
	}
}
