package com.fireboyev.discord.novus.commands.guild.censoring;

import java.util.concurrent.TimeUnit;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.util.Bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CensorConfigCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			if (args.length == 1) {
				GuildFolder folder = FileManager.openGuildFolder(guild);
				EmbedBuilder builder = new EmbedBuilder();
				builder.setAuthor(member.getEffectiveName());
				builder.setColor(member.getColor());
				builder.setTitle("Censor Config");
				builder.setDescription("Do " + folder.getCommandPrefix() + "censorconfig [option] <arg>");
				builder.addField("1 - Enabled", Boolean.toString(folder.options.censoring.isEnabled), false);
				builder.addField("2 - Admin Bypass", Boolean.toString(folder.options.censoring.adminBypass), false);
				builder.addField("3 - Warn Users", Boolean.toString(folder.options.censoring.shouldWarn), false);
				builder.addField("4 - Warn Message", folder.options.censoring.warnMessage, false);
				builder.addField("Add",
						"Use **" + folder.getCommandPrefix() + "censorconfig add <censor>** to add censors", false);
				builder.addField("Remove",
						"Use **" + folder.getCommandPrefix() + "censorconfig remove <censor>** to remove censors",
						false);
				builder.addField("List",
						"Use **" + folder.getCommandPrefix() + "censorconfig list** to list all censored words", false);
				channel.sendMessage(builder.build()).queue();
			} else if (args.length > 1) {
				GuildFolder folder = FileManager.openGuildFolder(guild);
				if (args[1].equalsIgnoreCase("1")) {
					folder.options.censoring.isEnabled = !folder.options.censoring.isEnabled;
					folder.save();
					if (folder.options.censoring.isEnabled)
						channel.sendMessage("Censoring is now enabled!").queue();
					else
						channel.sendMessage("Censoring is now disabled!").queue();
				} else if (args[1].equalsIgnoreCase("2")) {
					folder.options.censoring.adminBypass = !folder.options.censoring.adminBypass;
					folder.save();
					if (folder.options.censoring.adminBypass)
						channel.sendMessage("Admin Bypass enabled!").queue();
					else
						channel.sendMessage("Admin Bypass disabled!").queue();
				} else if (args[1].equalsIgnoreCase("3")) {
					folder.options.censoring.shouldWarn = !folder.options.censoring.shouldWarn;
					folder.save();
					if (folder.options.censoring.shouldWarn)
						channel.sendMessage("User Warning enabled!").queue();
					else
						channel.sendMessage("User Warning disabled!").queue();
				} else if (args[1].equalsIgnoreCase("4")) {
					if (args.length > 2) {
						String msg = message.getContentRaw().substring(args[0].length() + args[1].length() + 2);
						folder.options.censoring.warnMessage = msg;
						folder.save();
						channel.sendMessage("Censor Warn Message set to: " + msg).queue();
					} else {
						channel.sendMessage("Not Enough Arguments!").queue();
					}
				} else if (args[1].equalsIgnoreCase("add")) {
					if (args.length == 3) {
						folder.options.censoring.censoredWords.add(args[2].toLowerCase());
						folder.save();
						Message m = channel.sendMessage("Censor '" + args[2].toLowerCase()
								+ "' added. This message will be deleted in 10 seconds").complete();
						try {
							m.delete().queueAfter(10, TimeUnit.SECONDS);
						} catch (Exception e) {

						}
					} else if (args.length == 2) {
						channel.sendMessage("Not Enough Arguments!").queue();
					} else {
						channel.sendMessage("Too Many Arguments!").queue();
					}
				} else if (args[1].equalsIgnoreCase("remove")) {
					if (args.length == 3) {
						boolean found = folder.options.censoring.censoredWords.remove(args[2].toLowerCase());
						if (found) {
							folder.save();
							Message m = channel.sendMessage("Censor '" + args[2].toLowerCase()
									+ "' removed. This message will be deleted in 10 seconds").complete();
							try {
								m.delete().queueAfter(10, TimeUnit.SECONDS);
							} catch (Exception e) {

							}
						} else {
							Message m = channel
									.sendMessage("Censor '" + args[2].toLowerCase()
											+ "' isn't censored. This message will be deleted in 10 seconds")
									.complete();
							try {
								m.delete().queueAfter(10, TimeUnit.SECONDS);
							} catch (Exception e) {

							}
						}
					} else if (args.length == 2) {
						channel.sendMessage("Not Enough Arguments!").queue();
					} else {
						channel.sendMessage("Too Many Arguments!").queue();
					}
				}
			}
		} else {
			channel.sendMessage("Sorry, You don't have permission to do this command!").queue();
		}
	}
}
