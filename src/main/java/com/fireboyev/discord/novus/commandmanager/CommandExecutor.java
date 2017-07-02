package com.fireboyev.discord.novus.commandmanager;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface CommandExecutor {
	void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel, GuildMessageReceivedEvent event);
}
