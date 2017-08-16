package com.fireboyev.discord.novus.commands.user;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class BadgeCommand implements CommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		GuildFolder folder = FileManager.openGuildFolder(guild);
		if (args.length == 1) {
			channel.sendMessage("Usage: " + folder.getCommandPrefix() + "badge equip|purchase").queue();
			return;
		}
		if (args.length == 2) {
			
		}
	}
}
