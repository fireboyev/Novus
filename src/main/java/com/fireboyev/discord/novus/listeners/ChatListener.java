/*
 *     Copyright (C) <2017>  <Evan Penner / fireboyev>
 *
 *  Novus is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Novus is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Novus.  If not, see <http://www.gnu.org/licenses/>.
 */package com.fireboyev.discord.novus.listeners;

import java.io.File;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.util.Bot;
import com.fireboyev.discord.novus.util.Formatter;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChatListener extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split(" ");
		// System.out.println("{" + event.getGuild().getName() + ":" +
		// event.getChannel().getName() + "}<"
		// + event.getAuthor().getName() + "> " + event.getMessage().getContent());
		if (!event.getAuthor().isBot()) {
			if (event.getMessage().getContentDisplay().equalsIgnoreCase(">shutdown")) {
				if (event.getAuthor().getId().equals("223230587157217280")) {
					event.getChannel().sendMessage("am sorri I'll go now ;-;").queue();
						//Main.server.stop(0);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Main.getJda().shutdownNow();
						System.exit(0);
				} else {
					//event.getChannel().sendMessage("Only My Master Can Do That!");
				}
			}
			if (event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser())) {
				if (!event.getMessage().mentionsEveryone()) {
					event.getChannel()
							.sendMessage("Use **" + FileManager.openGuildFolder(event.getGuild()).getCommandPrefix()
									+ "** to use my commands!")
							.queue();
				}
			}
			if (args[0].equalsIgnoreCase(">deleteuserfiles")) {
				if (Bot.IsFire(event.getMember())) {
					event.getChannel().sendMessage("Deleting Files...").queue();
					long oldsysTime = System.currentTimeMillis();
					for (File file : FileManager.getMainUsersFolder().listFiles()) {
						for (File miniFile : file.listFiles()) {
							miniFile.delete();
						}
						file.delete();
					}
					long currentsysTime = System.currentTimeMillis();
					event.getChannel()
							.sendMessage("Files Deleted in " + Long.toString(currentsysTime - oldsysTime) + " ms")
							.queue();
				}
			}
			if (args[0].equalsIgnoreCase(">createdefaultfiles")) {
				if (Bot.IsFire(event.getMember())) {
					event.getChannel().sendMessage("Generating Files...").queue();
					long oldsysTime = System.currentTimeMillis();
					FileManager.openGuildFolder(event.getGuild());
					for (Member member : event.getGuild().getMembers()) {
						FileManager.openUserFolder(member.getUser());
					}
					long currentsysTime = System.currentTimeMillis();
					event.getChannel()
							.sendMessage("Files Generated in " + Long.toString(currentsysTime - oldsysTime) + " ms")
							.queue();

				} else {
					event.getChannel().sendMessage("Sorry, You Don't Have Permission To Do This.").queue();
				}
			}
			if (args[0].equalsIgnoreCase(">folder")) {
				if (Bot.IsFire(event.getMember())) {
					event.getChannel()
							.sendMessage("Bot Folder is Located in: " + FileManager.getBotFolder().getAbsolutePath())
							.queue();
				}
			}
			GuildFolder folder = FileManager.openGuildFolder(event.getGuild());
			if (folder.options.censoring.isEnabled) {
				if (folder.options.censoring.adminBypass) {
					if (Bot.IsAdmin(event.getMember())) {
						return;
					}
				}
				if (event.getMessage().getContentDisplay().startsWith(folder.getCommandPrefix() + "censorconfig")
						&& Bot.IsAdmin(event.getMember()))
					return;
				if (Main.censoring.containsCensor(event.getMessage().getContentDisplay(), event.getGuild())) {
					try {
						event.getMessage().delete().queue();
					} catch (Exception e) {

					}
					if (folder.options.censoring.shouldWarn) {
						event.getChannel().sendMessage(Formatter.formatMessage(folder.options.censoring.warnMessage,
								event.getMember(), event.getGuild())).queue();
					}
				}
			}
		}
	}
}
