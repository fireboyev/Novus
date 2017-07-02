package com.fireboyev.discord.selfbot.commands.util;

import com.fireboyev.discord.selfbot.Bot;
import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PurgeCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (Bot.IsAdmin(event.getMember())) {
			int num = 10;
			try {
				int tempnum = Integer.parseInt(args[1]);
				if (tempnum > 1)
					num = tempnum;
			} catch (Exception e) {

			}
			MessageHistory history = event.getChannel().getHistory();
			// for (Message message :
			// history.retrievePast(num).complete())
			// message.delete().queue();
			event.getChannel().deleteMessages(history.retrievePast(num).complete()).queue();
			event.getChannel().sendMessage(":information_source: Purge Complete For " + event.getAuthor().getAsMention()
					+ " :information_source:").queue();

		}
	}
}
