package com.fireboyev.discord.selfbot.commands.games;

import java.util.List;
import java.util.Random;

import com.fireboyev.discord.selfbot.FileManager;
import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ComplimentCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (args.length > 1) {
			StringBuilder builder = new StringBuilder(event.getMessage().getRawContent());
			builder.delete(0, args[0].length());
			Random rand = new Random();
			List<String> compliments = FileManager.openGuildFolder(event.getGuild()).getCompliments();
			if (compliments.size() > 0) {
				String compliment = compliments.get(rand.nextInt(compliments.size()));
				event.getChannel().sendMessage(builder.toString() + "," + compliment).queue();
			} else {
				event.getChannel().sendMessage("Guild Compliment List Is Empty!").queue();
			}
		} else {
			event.getChannel().sendMessage("Who should I compliment?").queue();
		}
	}
}
