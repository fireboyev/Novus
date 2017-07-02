package com.fireboyev.discord.selfbot;

import java.util.ArrayList;
import java.util.List;

import com.fireboyev.discord.selfbot.commandmanager.CommandDescription;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class Bot {
	static List<CommandDescription> commands = new ArrayList<CommandDescription>();

	public static void registerCommand(CommandDescription command) {
		commands.add(command);
	}

	public static List<CommandDescription> getCommands() {
		return commands;
	}

	public static boolean IsFire(Member user) {
		if (user.getUser().getId().equals("223230587157217280"))
			return true;
		return false;
	}

	public static boolean IsAdmin(Member user) {
		if (user.hasPermission(Permission.ADMINISTRATOR))
			return true;
		if (user.getUser().getId().equals("223230587157217280"))
			return true;
		if (user.isOwner())
			return true;
		for (Role role : user.getRoles()) {
			if (role.getName().equals("NovusAdmin")) {
				return true;
			}
		}
		return false;
	}
}
