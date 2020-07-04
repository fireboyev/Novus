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

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerInfoCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		String members = String.valueOf(guild.getMembers().size());
		String roles = String.valueOf(guild.getRoles().size());
		String channels = String.valueOf(guild.getTextChannels().size());
		builder.setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl());
		builder.addField("Owner:", guild.getOwner().getEffectiveName(), true);
		builder.setThumbnail(guild.getIconUrl());
		builder.addField("Number Of Members:", members, true);
		builder.addField("Number of Roles:", roles, true);
		builder.addField("Number of Channels:", channels, true);
		event.getChannel().sendMessage(builder.build()).queue();
	}
}
