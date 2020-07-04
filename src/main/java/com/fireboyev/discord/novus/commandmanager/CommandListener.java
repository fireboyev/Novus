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
 */package com.fireboyev.discord.novus.commandmanager;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!event.getAuthor().isBot()) {
			menuSelection(event);
			String[] args = event.getMessage().getContentRaw().split(" ");
			String cmdPrefix = "n!";
			if (event.getGuild() != null)
				cmdPrefix = FileManager.openGuildFolder(event.getGuild()).getCommandPrefix();
			if (args[0].startsWith(cmdPrefix)) {
				for (Command cmd : Main.cm.getCommands()) {
					if (cmd.getName().equalsIgnoreCase(args[0].replace(cmdPrefix, ""))) {
						Main.cm.executed++;
						cmd.executed++;
						try {
							if (event.getGuild() != null) {
								if (FileManager.openGuildFolder(event.getGuild()).options.isCmdBanned(cmd,
										event.getAuthor())) {
									event.getTextChannel()
											.sendMessage(":no_entry: Sorry " + event.getAuthor().getAsMention()
													+ ", You aren't allowed to use this command. :no_entry:")
											.queue();
									return;
								}
							}
							if (cmd.getExecutor() instanceof GuildCommandExecutor) {
								if (event.getGuild() != null) {
									((GuildCommandExecutor) cmd.getExecutor()).onCommand(event.getGuild(),
											event.getAuthor(), event.getMember(), event.getMessage(), args,
											event.getChannel(), event);
								}
							} else {
								((CommandExecutor) cmd.getExecutor()).onCommand(event.getAuthor(), event.getMessage(),
										args, event.getChannel(), event);
							}
						} catch (PermissionException e) {

						}
						break;
					}
				}
			}
		}
	}

	public void menuSelection(MessageReceivedEvent event) {
		Main.getBadgeManager().triggerMenu(event.getAuthor(), event.getMessage());
	}
}