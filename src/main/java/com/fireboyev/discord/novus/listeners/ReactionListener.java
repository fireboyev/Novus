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
import net.dv8tion.jda.core.entities.MessageReaction;
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
				Guild guild = event.getGuild();
				Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
				if (song != null) {
					if (!Main.getMusicManager().getGuildAudioPlayer(guild).player.getPlayingTrack().getIdentifier()
							.equalsIgnoreCase(song.getId()))
						event.getChannel().getMessageById(event.getMessageIdLong()).complete().getReactions().get(1).removeReaction().queue();
						return;
				}
				int count = 0;
				MessageReaction r = event.getReaction();
				for (User u : r.getUsers().complete()) {
					if (event.getGuild().getAudioManager().isConnected()) {
						if (event.getGuild().getAudioManager().getConnectedChannel().getMembers()
								.contains(event.getGuild().getMember(u))) {
							count++;
						}
					}

				}
				if (event.getGuild().getAudioManager().isConnected()) {
					int memberCount = event.getGuild().getAudioManager().getConnectedChannel().getMembers().size();
					memberCount -= 1;
					count -= 1;
					if (count >= Math.round(memberCount / 2)) {
						event.getChannel().sendMessage("Majourity of Users want to skip song ("
								+ (int) (Math.round(memberCount / 2)) + "/" + memberCount + ")").queue();
						if (r != null) {
							r.getTextChannel().getMessageById(r.getMessageId()).complete().getReactions().get(1)
									.removeReaction().queue();
						}
						Main.getMusicManager().skipTrack(event.getChannel());
					}
				}
			}
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		if (!event.getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
			if (event.getReactionEmote().getName().equals("â­�")) {
				Guild guild = event.getGuild();
				Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
				if (song != null) {
					User user = event.getUser();
					UserFolder folder = FileManager.openUserFolder(user);
					folder.removeSong(song);
					user.openPrivateChannel().complete().sendMessage("Removed from Song Favs: " + song.getName())
							.queue();
				}
			}
		}
	}
}
