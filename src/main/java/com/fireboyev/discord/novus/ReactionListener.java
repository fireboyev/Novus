package com.fireboyev.discord.novus;

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
