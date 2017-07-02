package com.fireboyev.discord.novus.commands.util;

import com.fireboyev.discord.novus.Bot;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SayCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (Bot.IsAdmin(event.getMember())) {
			channel.sendMessage(message.getRawContent().replace(">say ", "")).queue();
			message.delete().queue();

		} else {
			channel.sendMessage("Sorry " + event.getAuthor().getAsMention() + ", You Don't Have Permission to do This.")
					.queue();
		}
	}
}
