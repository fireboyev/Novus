package com.fireboyev.discord.novus.commands.util;

import com.fireboyev.discord.novus.LogManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class GetLogsCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		String completeMessage = "";
		for (Message msg : LogManager.getLog(event.getChannel(), 11)) {
			if (!msg.getId().equals(message.getId()))
				completeMessage += "<" + msg.getAuthor().getName() + "> " + msg.getRawContent() + "\n";
		}
		builder.addField("", completeMessage, false);
		builder.setTitle("Last 10 Message Actions for '" + event.getChannel().getName() + "'", null);
		event.getChannel().sendMessage(builder.build()).queue();

	}
}
