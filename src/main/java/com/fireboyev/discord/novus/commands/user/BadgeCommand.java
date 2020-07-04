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
 */package com.fireboyev.discord.novus.commands.user;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BadgeCommand implements CommandExecutor {

	@Override
	public void onCommand(User user, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		String prefix = "n!";
		if (event.getGuild() != null)
			prefix = FileManager.openGuildFolder(event.getGuild()).getCommandPrefix();
		if (args.length != 2) {
			channel.sendMessage("Usage: " + prefix + "badge equip|purchase").queue();
			return;
		} else {
			if (args[1].equalsIgnoreCase("purchase")) {
				Main.getBadgeManager().registerMenu(user, event.getTextChannel());
			} else if (args[1].equalsIgnoreCase("equip")) {

			} else {
				channel.sendMessage("scrub").queue();
			}
		}
	}
}
