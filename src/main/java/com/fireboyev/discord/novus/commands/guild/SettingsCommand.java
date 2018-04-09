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
 */package com.fireboyev.discord.novus.commands.guild;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SettingsCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			GuildFolder folder = FileManager.openGuildFolder(guild);
			boolean playlistEditing = folder.options.getPlaylist().canEdit();
			boolean playlistViewing = folder.options.getPlaylist().canView();
			boolean playlistPlaying = folder.options.getPlaylist().canPlay();
			if (args.length == 1) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle("Guild Settings for " + guild.getName());
				builder.setAuthor(member.getEffectiveName(), null, guild.getIconUrl());
				builder.addField("1. Playlist Editing", Boolean.toString(playlistEditing), false);
				builder.addField("2. Playlist Viewing", Boolean.toString(playlistViewing), false);
				builder.addField("3. Playlist Playing", Boolean.toString(playlistPlaying), false);
				builder.addField("4. Command Prefix", folder.getCommandPrefix(), false);
				builder.addField("5. Join Message - Use " + folder.getCommandPrefix() + "joinleave for more info",
						folder.options.joinMessage, false);
				builder.addField("6. Leave Message - Use " + folder.getCommandPrefix() + "joinleave for more info",
						folder.options.leaveMessage, false);
				if (folder.options.joinLeaveChannel == null) {
					builder.addField("7. Join/Leave Message Channel", "Not Set", false);
				} else
					builder.addField("7. Join/Leave Message Channel", Long.toString(folder.options.joinLeaveChannel),
							false);
				if (folder.options.musicVoiceChannel == null) {
					builder.addField("8. Music Voice Channel", "Not Set", false);
				} else
					builder.addField("8. Music Voice Channel", Long.toString(folder.options.musicVoiceChannel), false);
				if (folder.options.musicTextChannel == null) {
					builder.addField("9. Music Text Channel", "Not Set", false);
				} else
					builder.addField("9. Music Text Channel", Long.toString(folder.options.musicVoiceChannel), false);
				if (folder.options.loggingChannel == null) {
					builder.addField("10. Logger Channel", "Not Set", false);
				} else
					builder.addField("10. Logger Channel", Long.toString(folder.options.loggingChannel), false);
				channel.sendMessage(builder.build()).queue();
			} else if (args.length > 1) {
				if (args[1].equalsIgnoreCase("1")) {
					folder.options.getPlaylist().setEdit(!playlistEditing);
					folder.save();
					channel.sendMessage("Playlist Editing Set to: " + folder.options.getPlaylist().canEdit()).queue();
				} else if (args[1].equalsIgnoreCase("2")) {
					folder.options.getPlaylist().setView(!playlistViewing);
					folder.save();
					channel.sendMessage("Playlist Viewing Set to: " + folder.options.getPlaylist().canView()).queue();
				} else if (args[1].equalsIgnoreCase("3")) {
					folder.options.getPlaylist().setPlay(!playlistPlaying);
					folder.save();
					channel.sendMessage("Playlist Playing Set to: " + folder.options.getPlaylist().canPlay()).queue();
				} else if (args[1].equalsIgnoreCase("4")) {
					if (args.length == 3) {
						folder.setCommandPrefix(args[2]);
						channel.sendMessage("Command Prefix Set to: **" + args[2] + "**").queue();
					} else {
						channel.sendMessage("This Option Requires an Argument.").queue();
					}
				} else if (args[1].equalsIgnoreCase("5")) {
					if (args.length == 2) {
						channel.sendMessage("Join Message Removed.").queue();
					} else {
						String joinMessage = message.getContentDisplay().substring(
								args[0].length() + args[1].length() + 2, message.getContentDisplay().length());
						folder.options.joinMessage = joinMessage;
						folder.save();
						channel.sendMessage("Join Message Set To: " + joinMessage).queue();
					}
				} else if (args[1].equalsIgnoreCase("6")) {
					if (args.length == 2) {
						channel.sendMessage("Leave Message Removed.").queue();
					} else {
						String leaveMessage = message.getContentDisplay().substring(
								args[0].length() + args[1].length() + 2, message.getContentDisplay().length());
						folder.options.leaveMessage = leaveMessage;
						folder.save();
						channel.sendMessage("Leave Message Set To: " + leaveMessage).queue();
					}
				} else if (args[1].equalsIgnoreCase("7")) {
					folder.options.joinLeaveChannel = channel.getIdLong();
					folder.save();
					channel.sendMessage("this channel is now set as the Join/Leave Channel.").queue();
				} else if (args[1].equalsIgnoreCase("8")) {
					if (member.getVoiceState().inVoiceChannel()) {
						folder.options.musicVoiceChannel = member.getVoiceState().getChannel().getIdLong();
						folder.save();
						channel.sendMessage(
								"Music Voice Channel set to **" + member.getVoiceState().getChannel().getName() + "**")
								.queue();
					} else {
						channel.sendMessage("You must be in a voice channel to set this option!").queue();
					}
				} else if (args[1].equalsIgnoreCase("9")) {
					folder.options.musicTextChannel = event.getTextChannel().getIdLong();
					folder.save();
					Main.musicManager.getGuildAudioPlayer(guild).scheduler.setTextChannel(event.getTextChannel());
					channel.sendMessage("Music Text Channel set to **" + event.getTextChannel().getName() + "**")
							.queue();
				} else if (args[1].equalsIgnoreCase("10")) {
					folder.options.loggingChannel = channel.getIdLong();
					folder.save();
					channel.sendMessage("This channel is now set as the Logger Channel.").queue();
				} else {
					channel.sendMessage("Usage: " + folder.getCommandPrefix() + "settings <index>").queue();
				}
			}
		} else
			channel.sendMessage("Sorry " + member.getAsMention() + ", You Don't Have Permission to do this.").queue();
	}
}
