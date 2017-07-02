package com.fireboyev.discord.novus.commands.util;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ServerInfoCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		String members = String.valueOf(guild.getMembers().size());
		String roles = String.valueOf(guild.getRoles().size());
		String channels = String.valueOf(guild.getTextChannels().size());
		builder.setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl());
		builder.addField("Owner:", guild.getOwner().getEffectiveName(), true);
		builder.setThumbnail(guild.getIconUrl());
		builder.addField("Number Of Members:", members, true);
		builder.addField("Number of Roles:", roles, true);
		builder.addField("Number of Channels:", channels, true);
		event.getChannel().sendMessage(builder.build()).queue();
	}
}
