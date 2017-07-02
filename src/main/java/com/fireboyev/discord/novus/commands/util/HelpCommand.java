package com.fireboyev.discord.novus.commands.util;

import java.util.List;

import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.Command;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User author, Member member, Message message, String[] args,
			MessageChannel channel, GuildMessageReceivedEvent event) {
		// Guild guild = event.getGuild();
		String prefix = FileManager.openGuildFolder(guild).getCommandPrefix();
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
				channel.sendMessage(builder.build()).queue();
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
							FileManager.openGuildFolder(guild).getCommandPrefix()), true);
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
