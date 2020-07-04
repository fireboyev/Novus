/*
 *     Copyright (C) <2017>  <Evan Penner / fireboyev>
 *
 *  Novus is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Novus is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Novus.  If not, see <http://www.gnu.org/licenses/>.
 */package com.fireboyev.discord.novus.commands.util;

import java.util.List;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.Command;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand implements CommandExecutor {
	@Override
	public void onCommand(User author, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		// Guild guild = event.getGuild();
		String prefix = "n!";
		if (event.getGuild()!=null){
			prefix = FileManager.openGuildFolder(event.getGuild()).getCommandPrefix();
		}
		List<Command> commands = Main.cm.getCommands();
		if (args[0].equalsIgnoreCase(prefix + "help") || args[0].equalsIgnoreCase(prefix + "?")) {
			if (args.length == 1) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setAuthor(author.getName(), null, author.getAvatarUrl());
				builder.setTitle("Help Menu", null);
				builder.addBlankField(false);
				for (Command command : commands) {
					if (command.getDescription().isVisible)
						builder.addField("- " + command.getName(), "", true);
				}
				builder.setFooter("Use >help <Command> to see information for a command", null);
				author.openPrivateChannel().complete().sendMessage(builder.build()).queue();
			} else if (args.length == 2) {
				Command currentCommand = null;
				for (Command command : commands) {
					if (command.getName().equalsIgnoreCase(args[1])) {
						currentCommand = command;
						break;
					}
				}
				if (currentCommand == null) {
					for (Command command : commands) {
						for (String alias : command.getDescription().getAliases()) {
							if (alias.equalsIgnoreCase(args[1])) {
								currentCommand = command;
								break;
							}
						}
					}
				}
				if (currentCommand != null) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setAuthor(author.getName(), null, author.getAvatarUrl());
					builder.setTitle(currentCommand.getName(), null);
					builder.setDescription(currentCommand.getDescription().getDescription());
					builder.addField("Usage: ", currentCommand.getDescription().getUsage().replace("%1",
							prefix), true);
					builder.addField("Aliases: ", "", false);
					for (String alias : currentCommand.getDescription().getAliases()) {
						builder.addField("- " + alias, "", true);
					}
					channel.sendMessage(builder.build()).queue();
				}
			}
		}
	}

}
