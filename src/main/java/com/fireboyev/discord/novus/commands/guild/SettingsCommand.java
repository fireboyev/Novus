package com.fireboyev.discord.novus.commands.guild;

import org.json.JSONObject;

import com.fireboyev.discord.novus.Bot;
import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.objects.GuildFolder;

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
			JSONObject json = folder.getJson();
			boolean playlistEditing = true;
			boolean playlistViewing = true;
			boolean playlistPlaying = true;
			JSONObject playlistJson = new JSONObject();
			if (!json.isNull("playlist")) {
				playlistJson = json.getJSONObject("playlist");
				if (!playlistJson.isNull("edit"))
					playlistEditing = playlistJson.getBoolean("edit");
				if (!playlistJson.isNull("view"))
					playlistViewing = playlistJson.getBoolean("view");
				if (!playlistJson.isNull("play"))
					playlistPlaying = playlistJson.getBoolean("play");
			}
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
					playlistEditing = !playlistEditing;
					playlistJson.put("edit", playlistEditing);
					json.put("playlist", playlistJson);
					folder.writeJsonToFile(json, folder.getConfigFile());
					channel.sendMessage("Playlist Editing Set to: " + playlistEditing).queue();
				} else if (args[1].equalsIgnoreCase("2")) {
					playlistViewing = !playlistViewing;
					playlistJson.put("view", playlistViewing);
					json.put("playlist", playlistJson);
					folder.writeJsonToFile(json, folder.getConfigFile());
					channel.sendMessage("Playlist Viewing Set to: " + playlistViewing).queue();
				} else if (args[1].equalsIgnoreCase("3")) {
					playlistPlaying = !playlistPlaying;
					playlistJson.put("play", playlistPlaying);
					json.put("playlist", playlistJson);
					folder.writeJsonToFile(json, folder.getConfigFile());
					channel.sendMessage("Playlist Playing Set to: " + playlistPlaying).queue();
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
		}
	}
}
