package com.fireboyev.discord.novus.commands.guild;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.Command;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FunctionUnbanCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (!Bot.IsAdmin(member)) {
			channel.sendMessage("You must have the Permission: MANAGE_SERVER to use this command!").queue();
			return;
		}
		if (args.length > 2) {
			String cmdName = args[1];
			cmdName = cmdName.replace(FileManager.openGuildFolder(guild).getCommandPrefix(), "");
			Command command = null;
			for (Command cmd : Main.cm.getCommands()) {
				if (cmd.getName().equalsIgnoreCase(cmdName)) {
					command = cmd;
					break;
				}
			}
			if (command == null) {
				channel.sendMessage("Command '" + cmdName + "' not found.").queue();
				return;
			}
			if (message.getMentionedMembers().size() > 0) {
				int count = 0;
				GuildFolder folder = FileManager.openGuildFolder(guild);
				for (Member m : message.getMentionedMembers()) {
					if (!folder.options.isCmdBanned(command, m.getUser()))
						continue;
					folder.options.cmdUnbanUser(command, m.getUser());
					count++;
				}
				folder.save();
				if (count != message.getMentionedMembers().size())
					channel.sendMessage(count + " user(s) were unbanned from using '" + command.getName() + "', "
							+ Integer.toString(message.getMentionedMembers().size() - count)
							+ " user(s) could not be or were already unbanned.").queue();
				else
					channel.sendMessage(count + " user(s) were unbanned from using '" + command.getName() + "'")
							.queue();
			} else {
				channel.sendMessage("None of the mentioned users were found").queue();
			}
		} else {
			channel.sendMessage("Command Syntax: **" + FileManager.openGuildFolder(guild).getCommandPrefix()
					+ "functionunban <Command> <@User>**").queue();
		}
	}

}
