package com.fireboyev.discord.novus.commands.bot;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ChannelSay implements CommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (Bot.IsFire(user)) {
			StringBuilder builder = new StringBuilder(event.getMessage().getRawContent());
			builder.delete(0, args[0].length());
			builder.delete(0, args[1].length());
			builder.delete(0, 1);
			event.getJDA().getGuildById(args[1]).getPublicChannel().sendMessage(builder.toString()).queue();
		}
	}

}
