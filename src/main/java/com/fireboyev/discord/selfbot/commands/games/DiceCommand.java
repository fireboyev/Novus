package com.fireboyev.discord.selfbot.commands.games;

import java.util.Random;

import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class DiceCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		Random r = new Random();
		channel.sendMessage(
				"**" + member.getEffectiveName() + " Rolled a " + Integer.toString(r.nextInt(5) + 1) + "!**").queue();
	}
}
