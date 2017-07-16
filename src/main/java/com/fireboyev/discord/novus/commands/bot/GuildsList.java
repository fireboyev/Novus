package com.fireboyev.discord.novus.commands.bot;

import com.fireboyev.discord.novus.Bot;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class GuildsList implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			String str = "";
			int count = 0;
			for (Guild g : event.getJDA().getGuilds()) {
				str += g.getName() + "\n";
				if (count > 15)
					break;
				count++;
			}
			channel.sendMessage(str).queue();
		}
	}
}
