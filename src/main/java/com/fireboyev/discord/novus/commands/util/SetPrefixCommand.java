package com.fireboyev.discord.novus.commands.util;

import com.fireboyev.discord.novus.Bot;
import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SetPrefixCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			if (args.length == 2) {
				FileManager.openGuildFolder(guild).setCommandPrefix(args[1]);
				channel.sendMessage("Command Prefix Set to: **" + args[1] + "**").queue();
			} else {
				channel.sendMessage("**Incorrect Arguments!").queue();
			}
		} else {
			channel.sendMessage("**Sorry, " + member.getAsMention() + ", You can't do this command.").queue();
		}
	}
}
