package com.fireboyev.discord.novus.commands.games;

import com.fireboyev.discord.novus.FileManager;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class AddInsultCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		StringBuilder builder = new StringBuilder(message.getContent());
		builder.delete(0, args[0].length());
		if (builder.toString().length() > 5) {
			FileManager.openGuildFolder(event.getGuild()).addInsult(builder.toString());
			channel.sendMessage("Successfully Added: '" + builder.toString() + "' to Guild Insults List").queue();
		} else {
			channel.sendMessage("Insults Must Be Longer Than 5 Characters!").queue();
		}
	}
}
