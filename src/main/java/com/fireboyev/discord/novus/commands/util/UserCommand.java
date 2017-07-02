package com.fireboyev.discord.novus.commands.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class UserCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User author, Member member, Message message, String[] args,
			MessageChannel channel, GuildMessageReceivedEvent event) {
		if (args.length == 2) {
			if (event.getMessage().getMentionedUsers() != null && event.getMessage().getMentionedRoles().size() == 0) {
				EmbedBuilder builder = new EmbedBuilder();
				User user = event.getMessage().getMentionedUsers().get(0);
				builder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
				builder.setColor(event.getGuild().getMember(user).getColor());
				builder.setTitle(user.getName() + "'s Info", user.getAvatarUrl());
				builder.addField("ID:", user.getId(), false);
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(author.getIdLong());
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				dateFormat.setTimeZone(cal.getTimeZone());
				builder.addField("Account Created", dateFormat.format(cal.getTime()), false);
				builder.setThumbnail(user.getAvatarUrl());
				builder.addField("Roles:", "(" + event.getGuild().getMember(user).getRoles().size() + ")", true);
				if (user.isBot())
					builder.addField("isBot:", "true", false);
				else
					builder.addField("isBot:", "false", false);
				event.getChannel().sendMessage(builder.build()).queue();
			} else {
				event.getChannel().sendMessage("``**Usage: ** >info @(User)``").queue();
			}
		} else {
			event.getChannel().sendMessage("``**Usage: ** >info @(User)``").queue();
		}
	}
}
