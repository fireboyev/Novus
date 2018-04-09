package com.fireboyev.discord.novus.commands.guild;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.util.Bot;
import com.fireboyev.discord.novus.util.Formatter;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class JoinLeaveCommand implements GuildCommandExecutor{

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			if (args.length > 2)
				if (args[2].equalsIgnoreCase("test")) {
					channel.sendMessage("**Current Join Message:** " + Formatter.formatJoinMessage(FileManager.openGuildFolder(guild).options.joinMessage, member, guild) + "\n**Current Leave Message:** " + Formatter.formatLeaveMessage(FileManager.openGuildFolder(guild).options.leaveMessage, member, guild)).queue();
					return;
				}
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Join & Leave Message Help");
			builder.setAuthor(member.getEffectiveName());
			builder.setColor(member.getColor());
			builder.appendDescription("These are the prefixes you can use for join & leave messages.");
			String currentJoinMessage = FileManager.openGuildFolder(guild).options.joinMessage;
			String currentLeaveMessage = FileManager.openGuildFolder(guild).options.leaveMessage;
			if (currentJoinMessage.equalsIgnoreCase(""))
				currentJoinMessage = "None";
			if (currentLeaveMessage.equalsIgnoreCase(""))
				currentLeaveMessage = "None";
			builder.addField("Current Join Message", currentJoinMessage, false);
			builder.addField("Current Leave Message", currentLeaveMessage, false);
			builder.addField("User Name", "%user", false);
			builder.addField("User as mention", "%usermention", false);
			builder.addField("Guild Name", "%guild", false);
			builder.addField("Member Count", "%membercount", false);
			builder.addField("Owner of this Guild", "%owner", false);
			builder.setFooter("Hint: use **" + FileManager.openGuildFolder(guild).getCommandPrefix() +"joinleave test** - to see a test join and leave messsage", null);
			channel.sendMessage(builder.build()).queue();
		} else {
			channel.sendMessage("Sorry, you don't have permission to do this command!").queue();
		}
		
	}

}
