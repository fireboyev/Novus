package com.fireboyev.discord.novus.util;

import java.awt.Color;
import java.time.Instant;

import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

public enum GuildLogger {
	INSULT("%user Added an insult", "Insult"), COMPLIMENT("%user Added a compliment", "Compliment");
	private String format;
	private String argName;

	private GuildLogger(String format, String argName) {
		this.format = format;
		this.argName = argName;
	}

	private EmbedBuilder getBuilt(Member member, String arg) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl());
		String formatted = format;
		formatted = formatted.replace("%user", member.getUser().getName());
		builder.setDescription(formatted);
		builder.setTitle(argName + " Action");
		builder.setColor(Color.BLUE);
		builder.setThumbnail(member.getUser().getAvatarUrl());
		builder.addField(argName, arg, true);
		builder.setTimestamp(Instant.now());
		return builder;
	}

	public void log(Member member, String arg) {
		Guild g = member.getGuild();
		if (FileManager.openGuildFolder(g).options.loggingChannel != null) {
			TextChannel channel = g.getTextChannelById(FileManager.openGuildFolder(g).options.loggingChannel);
			if (channel != null) {
				if (channel.canTalk()) {
					channel.sendMessage(getBuilt(member, arg).build()).queue();
				}
			}
		}
	}
}
