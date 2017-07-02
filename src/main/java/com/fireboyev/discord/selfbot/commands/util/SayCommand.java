package com.fireboyev.discord.selfbot.commands.util;

import com.fireboyev.discord.selfbot.Bot;
import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SayCommand extends CommandExecutor {
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
