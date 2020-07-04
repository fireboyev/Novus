package com.fireboyev.discord.novus.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;

public class Formatter {
	public static String formatJoinMessage(String msg, GuildMemberJoinEvent event) {
		return formatJoinMessage(msg, event.getMember(), event.getGuild());
	}

	public static String formatJoinMessage(String msg, Member member, Guild guild) {
		String newMsg = msg;
		newMsg = newMsg.replace("%usermention", member.getAsMention());
		newMsg = newMsg.replace("%user", member.getEffectiveName());
		newMsg = newMsg.replace("%guild", guild.getName());
		newMsg = newMsg.replace("%membercount", Integer.toString(guild.getMembers().size()));
		newMsg = newMsg.replace("%owner", guild.getOwner().getEffectiveName());
		return newMsg;
	}

	public static String formatLeaveMessage(String msg, GuildMemberLeaveEvent event) {
		return formatLeaveMessage(msg, event.getMember(), event.getGuild());
	}

	public static String formatLeaveMessage(String msg, Member member, Guild guild) {
		String newMsg = msg;
		newMsg = newMsg.replace("%usermention", member.getAsMention());
		newMsg = newMsg.replace("%user", member.getEffectiveName());
		newMsg = newMsg.replace("%guild", guild.getName());
		newMsg = newMsg.replace("%membercount", Integer.toString(guild.getMembers().size()));
		newMsg = newMsg.replace("%owner", guild.getOwner().getEffectiveName());
		return newMsg;
	}

	public static String formatMessage(String msg, Member member, Guild guild) {
		String newMsg = msg;
		newMsg = newMsg.replace("%usermention", member.getAsMention());
		newMsg = newMsg.replace("%user", member.getEffectiveName());
		newMsg = newMsg.replace("%guild", guild.getName());
		newMsg = newMsg.replace("%membercount", Integer.toString(guild.getMembers().size()));
		newMsg = newMsg.replace("%owner", guild.getOwner().getEffectiveName());
		return newMsg;
	}

	public static int CharToInt(char c) {
		return (int) c;
	}
}
