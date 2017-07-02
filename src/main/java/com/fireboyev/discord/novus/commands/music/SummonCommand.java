package com.fireboyev.discord.novus.commands.music;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SummonCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (!event.getGuild().getAudioManager().isConnected()) {
			event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
		} else {
			event.getChannel().sendMessage("***I am already connected to a Voice Channel!***").queue();
		}
	}
}
