package com.fireboyev.discord.novus.commands.guild;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SettingsCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
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
				} else {
					channel.sendMessage("Usage: " + folder.getCommandPrefix() + "settings <index>").queue();
				}
			}
		} else
			channel.sendMessage("Sorry " + member.getAsMention() + ", You Don't Have Permission to do this.").queue();
	}
}
