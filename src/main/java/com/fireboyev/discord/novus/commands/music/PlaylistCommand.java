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
import java.util.concurrent.TimeUnit;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlaylistCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		GuildFolder guildFolder = FileManager.openGuildFolder(guild);
		boolean view = guildFolder.options.getPlaylist().canView();
		boolean play = guildFolder.options.getPlaylist().canPlay();
		boolean edit = guildFolder.options.getPlaylist().canEdit();
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase("view")) {
				if (view) {
					UserFolder folder = FileManager.openUserFolder(user);
					List<Song> songs = folder.getSongs();
					if (songs.size() == 0) {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setTitle("Playlist - Page " + 1 + "/" + 0);
						builder.setAuthor(member.getEffectiveName(), null, user.getAvatarUrl());
						channel.sendMessage(builder.build()).queue();
						return;
					}
					List<Song[]> songListArray = new ArrayList<>();
					int count = 0;
					Song[] songArray = new Song[10];
					int totalCount = 1;
					for (Song song : songs) {
						songArray[count] = song;
						count++;
						totalCount++;
						if (count >= 10) {
							count = 0;
							songListArray.add(songArray);
							songArray = new Song[10];
							continue;
						}
						if (totalCount == songs.size()) {
							songListArray.add(songArray);
						}
					}
					if (args.length == 3) {
						try {
							int pagenum = Integer.parseInt(args[2]);
							if (songListArray.size() >= pagenum) {
								channel.sendMessage(buildMessage(songListArray.get(pagenum - 1), songListArray.size(),
										pagenum, member)).queue();
							} else {
								EmbedBuilder builder = new EmbedBuilder();
								builder.setTitle("Playlist - Page " + pagenum + "/" + songListArray.size());
								builder.setAuthor(member.getEffectiveName(), null, user.getAvatarUrl());
								channel.sendMessage(builder.build()).queue();
							}
						} catch (Exception e) {
							channel.sendMessage("Page Number Must Be An Integer!").queue();
							e.printStackTrace();
						}
					} else {
						channel.sendMessage("Invalid Arguments!").queue();
					}
				} else
					channel.sendMessage(":no_entry: Playlist Viewing is Disabled in this Guild! :no_entry:").queue();
			} else if (args[1].equalsIgnoreCase("play")) {
				if (play) {
					if (args.length == 3) {
						UserFolder folder = FileManager.openUserFolder(user);
						int index = -1;
						try {
							index = Integer.parseInt(args[2]) - 1;
						} catch (Exception e) {
							channel.sendMessage("Invalid Arguments, Usage: " + guildFolder.getCommandPrefix()
									+ "playlist play <index>").queue();
							return;
						}
						List<Song> songs = folder.getSongs();
						if (index > songs.size()) {
							channel.sendMessage("You don't have that many songs!").queue();
							return;
						}
						Song song = songs.get(index);
						if (song != null) {
							Main.getMusicManager().loadAndPlay(event.getTextChannel(), "https://www.youtube.com/watch?v=" + song.getId());
						} else
							channel.sendMessage("There was an error while loading your song.").queue();
					} else {
						channel.sendMessage(
								"Invalid Arguments, Usage: " + guildFolder.getCommandPrefix() + "playlist play <index>")
								.queue();
					}
				} else {
					channel.sendMessage(":no_entry: Playlist Playing is Disabled in this Guild! :no_entry:").queue();
				}
			} else if (args[1].equalsIgnoreCase("remove")) {
				if (edit) {
					if (args.length == 3) {
						UserFolder folder = FileManager.openUserFolder(user);
						if (args[2].equalsIgnoreCase("all")) {
							folder.RemoveAllSongs();
							channel.sendMessage("Successfully Cleared your Playlist.").queue();
						} else {
							int index = -1;
							try {
								index = Integer.parseInt(args[2]) - 1;
								if (index < 0)
									throw new Exception();
							} catch (Exception e) {
								channel.sendMessage("Invalid Arguments, Usage: " + guildFolder.getCommandPrefix()
										+ "playlist play <index>").queue();
								return;
							}
							List<Song> songs = folder.getSongs();
							if (index > songs.size()) {
								channel.sendMessage("You don't have that many songs!").queue();
								return;
							}
							Song song = songs.get(index);
							if (song != null) {
								//System.out.println("removing song:" + song.toString());
								folder.removeSong(song);
								channel.sendMessage("Successfully Removed " + song.getName() + " from your playlist")
										.queue();
							} else {
								channel.sendMessage("There was an error deleting the song from your playlist").queue();
							}
						}
					}
				} else {
					channel.sendMessage(":no_entry: Playlist Editting is Disabled in this Guild! :no_entry:").queue();
				}
			}
		} else {
			channel.sendMessage(getHelpMessage(member)).queue();
		}
	}

	public MessageEmbed getHelpMessage(Member member) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Playlist Help").setAuthor(member.getNickname(), null, member.getUser().getAvatarUrl())
				.addField("playlist view <Page>", "View your playlist", false);
		builder.addField("playlist play <Index>", "Play a song from your playlist", false);
		builder.addField("playlist delete <index>", "Delete a song from your playlist", false);
		return builder.build();
	}

	private MessageEmbed buildMessage(Song[] songArray, int pages, int pagenum, Member member) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("Playlist - Page " + pagenum + "/" + pages);
		builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
		int count = 1;
		int toAdd = (pagenum * 10) - 10;
		for (Song song : songArray) {

			if (song != null) {
				String content = count + toAdd + ". " + song.getName() + " by " + song.getAuthor();
				if (content.length() > 250) {
					content = content.substring(0, 250) + "...";
				}
				builder.addField(content,
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
