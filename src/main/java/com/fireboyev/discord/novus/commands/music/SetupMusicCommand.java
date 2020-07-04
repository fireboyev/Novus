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
 */package com.fireboyev.discord.novus.commands.music;

import java.util.ArrayList;
import java.util.List;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

public class SetupMusicCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			if (guild.getTextChannelsByName("music", true).size() == 0) {
				guild.createTextChannel("music").setTopic("The Music Channel").complete();
			}

			if (guild.getVoiceChannelsByName("Music", true).size() == 0) {
				ChannelAction ca = guild.createVoiceChannel("Music");
				List<Permission> allow = new ArrayList<Permission>();
				List<Permission> deny = new ArrayList<Permission>();
				deny.add(Permission.VOICE_SPEAK);
				ca.addPermissionOverride(guild.getPublicRole(), allow, deny);
				ca.complete();
			}
			event.getChannel().sendMessage("Music Setup Complete.").queue();
		} else {
			event.getChannel().sendMessage("You Don't Have Permission, " + event.getAuthor().getAsMention()).queue();
		}
	}
}
