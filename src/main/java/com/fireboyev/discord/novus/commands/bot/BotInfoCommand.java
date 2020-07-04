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

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.Command;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BotInfoCommand implements CommandExecutor {

	@Override
	public void onCommand(User user, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		Runtime.getRuntime().gc();

		StringBuilder sb = new StringBuilder();
		sb.append("```swift\n");
		sb.append("Memory Usage: "
				+ bytesToMegabytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "MB / "
				+ bytesToMegabytes(Runtime.getRuntime().maxMemory()) + "MB");
		sb.append("\n");
		sb.append("Users: " + event.getJDA().getUsers().size() + " Users");
		sb.append("\n");
		sb.append("Guilds: " + event.getJDA().getGuilds().size() + " Guilds");
		sb.append("\n");
		sb.append("Cached Users: " + FileManager.userSize() + "");
		sb.append("\n");
		sb.append("Cached Guilds: " + FileManager.guildSize() + "");
		sb.append("\n");
		sb.append("Commands: " + Main.cm.getCommands().size() + "");
		sb.append("\n");
		sb.append("Commands Executed This Session: " + Main.cm.executed);
		Command mostPopularCmd = Main.cm.getMostPopularCommand();
		if (mostPopularCmd != null) {
			sb.append("\n");
			sb.append("Most Popular Command This Session: '" + mostPopularCmd.getName() + "' executed " + mostPopularCmd.executed
					+ " times");
		}
		sb.append("\n");
		sb.append("Uptime: " + getDurationBreakdown(ManagementFactory.getRuntimeMXBean().getUptime()));
		sb.append("```");
		builder.addField("", sb.toString(), false);
		builder.addField("", "          **[Add to your server](https://bots.discord.pw/bots/283418267408662529)**",
				true);
		builder.addField("", "          **[Source Code](https://github.com/fireboyev/Novus/)**", true);
		builder.addField("", "          **[Join My Server](https://discord.gg/7Fdk2st)**", true);
		builder.setAuthor("Novus Info", "https://bots.discord.pw/bots/283418267408662529",
				event.getJDA().getSelfUser().getAvatarUrl());
		channel.sendMessage(builder.build()).queue();
	}

	private String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		sb.append(days);
		sb.append(" Days ");
		sb.append(hours);
		sb.append(" Hours ");
		sb.append(minutes);
		sb.append(" Minutes ");
		sb.append(seconds);
		sb.append(" Seconds");

		return sb.toString();
	}

	public static long bytesToMegabytes(long bytes) {
		return bytes / (1024L * 1024L);
	}

}
