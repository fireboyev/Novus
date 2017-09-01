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
 */package com.fireboyev.discord.novus.commands.bot;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class GuildsList implements CommandExecutor {
	@Override
	public void onCommand(User user, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsFire(user)) {
			if (args.length == 1) {
				String str = "";
				int count = 0;
				for (Guild g : event.getJDA().getGuilds()) {
					str += g.getName() + ":" + g.getId() + ":" + g.getOwner().getEffectiveName() + "\n";
					if (count > 21)
						break;
					count++;
				}
				channel.sendMessage(str).queue();
			} else if (args.length > 2) {
				if (args[2].equalsIgnoreCase("channels")) {
					String str = "";
					for (TextChannel tc : event.getJDA().getGuildById(args[1]).getTextChannels()) {
						str += tc.getName() + ":" + tc.getId() + "\n";
					}
					channel.sendMessage(str).queue();
				}
				if (args[2].equalsIgnoreCase("members")) {
					String str = "";
					for (Member mem : event.getJDA().getGuildById(args[1]).getMembers()) {
						str += mem.getEffectiveName() + "\n";
					}
					channel.sendMessage(str).queue();
				}
				if (args[2].equalsIgnoreCase("history")) {
					String str = "";
					for (Message msg : event.getJDA().getTextChannelById(args[1]).getHistory().retrievePast(25)
							.complete()) {
						str += msg.getAuthor() + ": " + msg.getContent() + "\n";
					}
					channel.sendMessage(str).queue();
				}
				if (args[2].equalsIgnoreCase("invite")) {
					Invite invite = event.getJDA().getTextChannelById(args[1]).createInvite().complete();
					event.getJDA().getTextChannelById(args[1]).sendMessage("CREATED INVITE: " + invite.getCode())
							.queue();
					channel.sendMessage(invite.getCode()).queue();
				}
				if (args[2].equalsIgnoreCase("message")) {
					StringBuilder msg = new StringBuilder();
					for (String arg : args) {
						msg.append(arg + " ");
					}
					msg.delete(0, args[0].length() + 1);
					msg.delete(0, args[1].length() + 1);
					msg.delete(0, args[2].length() + 1);
					event.getJDA().getTextChannelById(args[1]).sendMessage(msg.toString())
							.queue(m -> channel.sendMessage(
									"Sent Message: ``" + m.getContent() + "`` to Channel: " + m.getChannel().getName())
									.queue());
				}
				if (args[2].equalsIgnoreCase("leave")) {
					event.getJDA().getGuildById(args[1]).leave().complete();
					channel.sendMessage("Left Guild Successfully!").queue();
				}
			}
		}
	}
}
