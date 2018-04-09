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
 */package com.fireboyev.discord.novus.listeners;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.util.Formatter;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		// server count stuff
		event.getJDA().getPresence()
				.setGame(Game.of(GameType.WATCHING, "over " + event.getJDA().getGuilds().size() + " Guilds"));
		Main.dbl.updateDiscordBotLists(event.getJDA().getGuilds().size());
		Main.dbla2.setStats(event.getJDA().getSelfUser().getId(), event.getJDA().getGuilds().size());
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		// join messages
		if (FileManager.openGuildFolder(event.getGuild()).options.joinLeaveChannel != null) {
			if (!FileManager.openGuildFolder(event.getGuild()).options.joinMessage.equalsIgnoreCase("")) {
				TextChannel channel = event.getJDA()
						.getTextChannelById(FileManager.openGuildFolder(event.getGuild()).options.joinLeaveChannel);
				if (channel != null) {
					if (channel.canTalk()) {
						channel.sendMessage(Formatter.formatJoinMessage(
								FileManager.openGuildFolder(event.getGuild()).options.joinMessage, event)).queue();
					}
				}
			}
		}
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		// leave messages
		if (FileManager.openGuildFolder(event.getGuild()).options.joinLeaveChannel != null) {
			if (!FileManager.openGuildFolder(event.getGuild()).options.leaveMessage.equalsIgnoreCase("")) {
				TextChannel channel = event.getJDA()
						.getTextChannelById(FileManager.openGuildFolder(event.getGuild()).options.joinLeaveChannel);
				if (channel != null) {
					if (channel.canTalk()) {
						channel.sendMessage(Formatter.formatLeaveMessage(
								FileManager.openGuildFolder(event.getGuild()).options.leaveMessage, event)).queue();
					}
				}
			}
		}
	}
}
