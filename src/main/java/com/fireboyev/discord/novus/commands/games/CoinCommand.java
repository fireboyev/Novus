package com.fireboyev.discord.novus.commands.games;

import java.util.Random;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CoinCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		Random r = new Random();
		if (r.nextBoolean()) {
			channel.sendMessage("**" + member.getEffectiveName() + " Flipped A Heads!**").queue();
		} else {
			channel.sendMessage("**" + member.getEffectiveName() + " Flipped A Tails!**").queue();
		}
	}
}
