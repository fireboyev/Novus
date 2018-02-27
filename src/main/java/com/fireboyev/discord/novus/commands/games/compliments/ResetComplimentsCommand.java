package com.fireboyev.discord.novus.commands.games.compliments;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ResetComplimentsCommand implements GuildCommandExecutor{

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			FileManager.openGuildFolder(guild).ResetCompliments();
			channel.sendMessage(":information_source: All guild compliments have been reset. :information_source:").queue();
		} else {
			channel.sendMessage(":exclamation: Only Members with the Administator Permission can use this Command!").queue();
		}
		
	}

}
