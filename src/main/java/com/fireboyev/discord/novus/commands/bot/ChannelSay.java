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
 */package com.fireboyev.discord.novus.commands.bot;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChannelSay implements CommandExecutor {

	@Override
	public void onCommand(User user, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsFire(user)) {
			StringBuilder builder = new StringBuilder(event.getMessage().getRawContent());
			builder.delete(0, args[0].length());
			builder.delete(0, args[1].length());
			builder.delete(0, 1);
			event.getJDA().getGuildById(args[1]).getPublicChannel().sendMessage(builder.toString()).queue();
		}
	}

}
