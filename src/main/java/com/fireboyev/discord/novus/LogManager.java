package com.fireboyev.discord.novus;

import java.util.List;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class LogManager {
	public static List<Message> getLog(TextChannel channel, int amount) {
		List<Message> log = channel.getHistory().retrievePast(amount).complete();
		return log;
	}
}
