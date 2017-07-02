package com.fireboyev.discord.novus.commands.util;

import java.awt.Color;

import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class InfoCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
		builder.setTitle("Bot Info", null);
		builder.addField("Hi, I'm Novus - a bot created by fireboyev",
				"If you need help, just do " + FileManager.openGuildFolder(guild).getCommandPrefix() + "help", false);
		builder.setColor(Color.ORANGE);
		builder.setImage(event.getJDA().getSelfUser().getAvatarUrl());
		event.getChannel().sendMessage(builder.build()).queue();

	}
}
