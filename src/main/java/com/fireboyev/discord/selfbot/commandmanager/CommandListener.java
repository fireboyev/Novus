package com.fireboyev.discord.selfbot.commandmanager;

import com.fireboyev.discord.selfbot.FileManager;
import com.fireboyev.discord.selfbot.Main;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			String[] args = event.getMessage().getRawContent().split(" ");
			String cmdPrefix = FileManager.openGuildFolder(event.getGuild()).getCommandPrefix();
			if (args[0].startsWith(cmdPrefix)) {
				for (Command cmd : Main.cm.getCommands()) {
					if (cmd.getName().equalsIgnoreCase(args[0].replace(cmdPrefix, ""))) {
						cmd.getExecutor().onCommand(event.getGuild(), event.getAuthor(), event.getMember(),
								event.getMessage(), args, event.getChannel(), event);
						break;
					}
				}
			}
		}
	}
}