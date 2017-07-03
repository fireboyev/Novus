package com.fireboyev.discord.novus.commands.music;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.music.Song;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.objects.UserFolder;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PlaylistCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		GuildFolder guildFolder = FileManager.openGuildFolder(guild);
		boolean view = true;
		JSONObject json = guildFolder.getJson();
		JSONObject playlistJson = null;
		if (!json.isNull("playlist")) {
			playlistJson = json.getJSONObject("playlist");
			if (!playlistJson.isNull("view")) {
				view = playlistJson.getBoolean("view");
			}
		}
		if (view) {
			UserFolder folder = FileManager.openUserFolder(user);
			List<Song> songs = folder.getSongs();
			List<Song[]> songListArray = new ArrayList<>();
			int count = 0;
			Song[] songArray = new Song[10];
			int totalCount = 1;
			for (Song song : songs) {
				if (count >= 10) {
					count = 0;
					songListArray.add(songArray);
					songArray = new Song[10];
				}
				songArray[count] = song;
				count++;
				totalCount++;
				if (totalCount == songs.size()) {
					songListArray.add(songArray);
				}
			}
			if (args.length == 1) {
				channel.sendMessage(buildMessage(songListArray.get(0), songListArray.size(), 1, member)).queue();
				return;
			}
			if (args.length == 2) {
				try {
					int pagenum = Integer.parseInt(args[1]);
					if (songListArray.size() >= pagenum)
						channel.sendMessage(
								buildMessage(songListArray.get(pagenum - 1), songListArray.size(), pagenum, member))
								.queue();
					else if (args.length == 3) {

					} else {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setTitle("Playlist - Page " + pagenum + "/" + songListArray.size());
						builder.setAuthor(member.getEffectiveName(), null, user.getAvatarUrl());
						channel.sendMessage(builder.build()).queue();
					}
				} catch (Exception e) {
					channel.sendMessage("Page Number Must Be An Integer!").queue();
				}
			} else {
				channel.sendMessage("Invalid Arguments!").queue();
			}
		} else
			channel.sendMessage(":no_entry: Playlist Viewing is Disabled in this Guild! :no_entry:").queue();
	}

	private MessageEmbed buildMessage(Song[] songArray, int pages, int pagenum, Member member) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Playlist - Page " + pagenum + "/" + pages);
		builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
		int count = 1;
		int toAdd = (pagenum * 10) - 10;
		for (Song song : songArray) {
			if (song != null) {
				builder.addField(count + toAdd + ". " + song.getName() + " by " + song.getAuthor(),
						"ID: " + song.getId() + ", Duration: " + getDurationBreakdown(song.getDuration()), true);
				count++;
			}
		}
		return builder.build();
	}

	private String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		sb.append(hours);
		sb.append(" Hours ");
		sb.append(minutes);
		sb.append(" Minutes ");
		sb.append(seconds);
		sb.append(" Seconds");

		return sb.toString();
	}
}
