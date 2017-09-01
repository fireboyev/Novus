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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UserCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User author, Member member, Message message, String[] args,
			MessageChannel channel, MessageReceivedEvent event) {
		if (args.length == 2) {
			if (event.getMessage().getMentionedUsers() != null && event.getMessage().getMentionedRoles().size() == 0) {
				EmbedBuilder builder = new EmbedBuilder();
				User user = event.getMessage().getMentionedUsers().get(0);
				builder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
				builder.setColor(event.getGuild().getMember(user).getColor());
				builder.setTitle(user.getName() + "'s Info", user.getAvatarUrl());
				builder.addField("ID:", user.getId(), false);
				Calendar cal = new GregorianCalendar();
				long epoch = (user.getIdLong() >> 22) + 1420070400000L;
				cal.setTimeInMillis(epoch);
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss");
				dateFormat.setTimeZone(cal.getTimeZone());
				dateFormat.setCalendar(cal);
				builder.addField("Account Created", dateFormat.format(cal.getTime()), false);
				builder.setThumbnail(user.getAvatarUrl());
				builder.addField("Roles:", "(" + event.getGuild().getMember(user).getRoles().size() + ")", true);
				if (user.isBot())
					builder.addField("isBot:", "true", false);
				else
					builder.addField("isBot:", "false", false);
				event.getChannel().sendMessage(builder.build()).queue();
			} else {
				event.getChannel().sendMessage("``**Usage: ** >info @(User)``").queue();
			}
		} else {
			event.getChannel().sendMessage("``**Usage: ** >info @(User)``").queue();
		}
	}
}
