package com.fireboyev.discord.novus.commands.games;

import java.util.List;
import java.util.Random;

import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class InsultCommand implements CommandExecutor {
@Override
public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
		GuildMessageReceivedEvent event) {
	if (args.length > 1) {
		StringBuilder builder = new StringBuilder(event.getMessage().getRawContent());
		builder.delete(0, args[0].length());
		Random rand = new Random();
		List<String> insults = FileManager.openGuildFolder(guild).getInsults();
		if (insults.size() > 0) {
			String insult = insults.get(rand.nextInt(insults.size()));
			channel.sendMessage(builder.toString() + "," + insult).queue();
		} else {
			channel.sendMessage("Guild Insult List Is Empty!").queue();
		}
	} else {
		channel.sendMessage("Who should I insult?").queue();
	}
}
}
