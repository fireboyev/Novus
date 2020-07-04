package com.fireboyev.discord.novus.commands.user;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemindCommand implements CommandExecutor{

	@Override
	public void onCommand(User user, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		String prefix = "!n";
		if (event.getGuild() != null)
			prefix = FileManager.openGuildFolder(event.getGuild()).getCommandPrefix();
		if (args.length > 1) {
			// TODO Finish this :p
		}
		else
			channel.sendMessage("I need to know when to remind you. Example: " + prefix + "remindme in 3d 5h to Update Novus\n It will then remind you in 3 Days, 5 Hours to 'Update Novus'").queue();
	}

}
