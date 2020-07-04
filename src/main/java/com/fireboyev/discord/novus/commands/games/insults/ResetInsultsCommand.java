package com.fireboyev.discord.novus.commands.games.insults;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ResetInsultsCommand implements GuildCommandExecutor{

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			FileManager.openGuildFolder(guild).ResetInsults();
			channel.sendMessage(":information_source: All guild insults have been reset. :information_source:").queue();
		} else {
			channel.sendMessage(":exclamation: Only Members with the Administator Permission can use this Command!").queue();
		}
		
	}

}
