package com.fireboyev.discord.novus.commands.music;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class QueueCommand implements CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		List<AudioTrack> songList = Main.getMusicManager().getQueue(event.getChannel());
		int count = 1;
		EmbedBuilder builder = new EmbedBuilder();
		long miliseconds = 0;
		for (AudioTrack track : songList) {
			builder.addField(Integer.toString(count) + ".", track.getInfo().title, true);
			miliseconds += track.getDuration();
			count++;
		}

		builder.setAuthor(member.getEffectiveName(), null, user.getAvatarUrl());
		builder.setTitle("Song Queue");
		builder.setColor(Color.BLUE);
		builder.setFooter("Current Queue Length: " + getDurationBreakdown(miliseconds), null);
		channel.sendMessage(builder.build()).queue();
	}

	public static String getDurationBreakdown(long millis) {
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
}
