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
import com.fireboyev.discord.novus.music.Song;
import com.fireboyev.discord.novus.objects.UserFolder;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (!event.getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
			if (event.getReactionEmote().getName().equals("⭐")) {
				Guild guild = event.getGuild();
				Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
				if (song != null) {
					User user = event.getUser();
					UserFolder folder = FileManager.openUserFolder(user);
					folder.addSong(song);
					user.openPrivateChannel().complete().sendMessage("Added to Song Favs: " + song.getName()).queue();
				}
			} else if (event.getReactionEmote().getName().equals("❌")) {

			}
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		if (!event.getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
			if (event.getReactionEmote().getName().equals("⭐")) {
				Guild guild = event.getGuild();
				Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
				if (song != null) {
					User user = event.getUser();
					UserFolder folder = FileManager.openUserFolder(user);
					folder.removeSong(song);
					user.openPrivateChannel().complete().sendMessage("Removed from Song Favs: " + song.getName())
							.queue();
				}
			} else if (event.getReactionEmote().getName().equals("❌")) {

			}
		}
	}
}
