package com.fireboyev.discord.novus.commands.util;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class InviteCommand implements CommandExecutor{

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		channel.sendMessage("<:discord:314003252830011395> **My Special Invite Link Filled With Love: https://bots.discord.pw/bots/283418267408662529**").queue();
		
	}

}
