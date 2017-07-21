package com.fireboyev.discord.novus.commands.games;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ReverseWordCommand implements CommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (args.length > 1) {
			StringBuilder builder = new StringBuilder(event.getMessage().getRawContent());
			builder.delete(0, args[0].length());
			String sentence = builder.reverse().toString();
			channel.sendMessage("``" + sentence + "``").queue();
		} else {
			channel.sendMessage("Invalid Arguments, I don't know what I should reverse!").queue();
		}

	}

}
