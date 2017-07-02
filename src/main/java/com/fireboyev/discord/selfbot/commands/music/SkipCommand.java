package com.fireboyev.discord.selfbot.commands.music;

import com.fireboyev.discord.selfbot.Main;
import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;
import com.fireboyev.discord.selfbot.music.BotMusicManager;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SkipCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (event.getGuild() != null) {
			BotMusicManager musicManager = Main.getMusicManager();

			musicManager.skipTrack(event.getChannel());
		}
	}
}
