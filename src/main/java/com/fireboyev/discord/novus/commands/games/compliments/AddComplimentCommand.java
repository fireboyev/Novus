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
 */package com.fireboyev.discord.novus.commands.games.compliments;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.util.GuildLogger;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AddComplimentCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		StringBuilder builder = new StringBuilder(event.getMessage().getContentDisplay());
		builder.delete(0, args[0].length() + 1);
		if (builder.toString().length() > 5) {
			if (FileManager.openGuildFolder(event.getGuild()).options.censoring.isEnabled)
				if (Main.censoring.containsCensor(builder.toString(), guild)) {
					channel.sendMessage("Your compliment contains a censored word!").queue();
					return;
				}
			FileManager.openGuildFolder(event.getGuild()).addCompliment(builder.toString());
			event.getChannel().sendMessage("Successfully Added: '" + builder.toString() + "' to Guild Compliments List")
					.queue();
			GuildLogger.COMPLIMENT.log(member, builder.toString());
		} else {
			event.getChannel().sendMessage("Compliments Must Be Longer Than 5 Characters!").queue();
		}
	}
}
