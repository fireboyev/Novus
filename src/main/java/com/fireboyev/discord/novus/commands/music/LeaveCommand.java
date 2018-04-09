package com.fireboyev.discord.novus.commands.music;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LeaveCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			if (guild.getAudioManager().isConnected()) {
				guild.getAudioManager().closeAudioConnection();
				channel.sendMessage(":notes: **I have left the music channel for you!**").queue();
			} else {
				guild.getAudioManager().closeAudioConnection();
				channel.sendMessage(":notes: **I wasn't even in the music channel, but okay!").queue();
			}
		}
	}

}
