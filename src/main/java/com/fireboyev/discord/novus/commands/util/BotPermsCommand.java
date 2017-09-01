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
 */package com.fireboyev.discord.novus.commands.util;

import java.awt.Color;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

public class BotPermsCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(event.getMember())) {
			if (args[1].equalsIgnoreCase("add")) {
				if (event.getMessage().getMentionedUsers().size() == 1) {
					if (args[3].equalsIgnoreCase("3")) {
						event.getGuild().getController()
								.addRolesToMember(
										event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)),
										event.getGuild().getRolesByName("NovusAdmin", false))
								.queue();
						event.getChannel()
								.sendMessage(":information_source: "
										+ event.getMessage().getMentionedUsers().get(0).getAsMention()
										+ " Added to NovusAdmin")
								.queue();
					}
					if (args[3].equalsIgnoreCase("2")) {
						event.getGuild().getController()
								.addRolesToMember(
										event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)),
										event.getGuild().getRolesByName("NovusMod", false))
								.queue();
						event.getChannel()
								.sendMessage(":information_source: "
										+ event.getMessage().getMentionedUsers().get(0).getAsMention()
										+ " Added to NovusMod")
								.queue();
					}
					if (args[3].equalsIgnoreCase("1")) {
						event.getGuild().getController()
								.addRolesToMember(
										event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)),
										event.getGuild().getRolesByName("NovusUser", false))
								.queue();
						event.getChannel()
								.sendMessage(":information_source: "
										+ event.getMessage().getMentionedUsers().get(0).getAsMention()
										+ " Added to NovusUser")
								.queue();
					}
				} else {
					event.getChannel()
							.sendMessage(event.getAuthor().getAsMention() + ", ``Usage: >botperms add @<User>``")
							.queue();
				}
			}
			if (args[1].equalsIgnoreCase("deleterole")) {
				if (Bot.IsAdmin(event.getMember())) {
					if (args.length == 3) {
						String rolename;
						Role role = event.getGuild().getRolesByName(args[2], true).get(0);
						if (role != null) {
							rolename = role.getName();
							role.delete().queue();
							event.getChannel().sendMessage("Successfully Deleted " + rolename + "!").queue();
						} else {
							event.getChannel().sendMessage("Can't Find a Role by the name of " + args[2] + "!").queue();
						}
					} else {
						event.getChannel().sendMessage("Wrong Arguments!").queue();
					}
				}
			}
			if (args[1].equalsIgnoreCase("createroles")) {
				if (event.getGuild().getRolesByName("NovusUser", false).isEmpty()) {
					RoleAction novususerrole = event.getGuild().getController().createRole();
					novususerrole.setName("NovusUser");
					novususerrole.setColor(Color.orange);
					novususerrole.setMentionable(false);
					novususerrole.setHoisted(false);
					novususerrole.complete();
				}
				if (event.getGuild().getRolesByName("NovusAdmin", false).isEmpty()) {
					RoleAction novusadminrole = event.getGuild().getController().createRole();
					novusadminrole.setName("NovusAdmin");
					novusadminrole.setPermissions(Permission.MANAGE_SERVER);
					novusadminrole.setPermissions(Permission.ADMINISTRATOR);
					novusadminrole.setColor(Color.orange);
					novusadminrole.setMentionable(false);
					novusadminrole.setHoisted(false);
					novusadminrole.complete();
				}
				if (event.getGuild().getRolesByName("NovusMod", false).isEmpty()) {
					RoleAction novusmodrole = event.getGuild().getController().createRole();
					novusmodrole.setName("NovusMod");
					novusmodrole.setColor(Color.orange);
					novusmodrole.setMentionable(false);
					novusmodrole.setHoisted(false);
					novusmodrole.complete();
				}
				event.getChannel()
						.sendMessage(":information_source: Roles have been updated " + event.getAuthor().getAsMention())
						.queue();
			}
		}
	}
}
