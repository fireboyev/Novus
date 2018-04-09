package com.fireboyev.discord.novus.commands.music;

import java.util.concurrent.TimeUnit;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayingCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		AudioTrack track = Main.musicManager.getGuildAudioPlayer(guild).player.getPlayingTrack();
		if (track != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("**:notes: Currently Playing** :notes:");
			builder.setTitle(track.getInfo().title, "https://www.youtube.com/watch?v=" + track.getIdentifier())
					.setAuthor(track.getInfo().author)
					.setImage("https://img.youtube.com/vi/" + track.getIdentifier() + "/hqdefault.jpg");
			builder.addField("Time",
					getFormattedTime(track.getPosition()) + " / " + getFormattedTime(track.getDuration()), true);
			channel.sendMessage(builder.build()).queue();
		} else {
			channel.sendMessage("**:notes: There are currently no songs being played right now. Play a song with "
					+ FileManager.openGuildFolder(guild).getCommandPrefix() + "play <song>**").queue();
		}
	}

	private String getFormattedTime(long millis) {
		if (TimeUnit.MILLISECONDS.toDays(millis) > 5)
			return "**Livestream**";
		long millisPos = millis;
		long hoursPos = TimeUnit.MILLISECONDS.toHours(millisPos);
		millisPos -= TimeUnit.HOURS.toMillis(hoursPos);
		long minPos = TimeUnit.MILLISECONDS.toMinutes(millisPos);
		millisPos -= TimeUnit.MINUTES.toMillis(minPos);
		long secondPos = TimeUnit.MILLISECONDS.toSeconds(millisPos);
		return hoursPos + ":" + minPos + ":" + secondPos;
	}

}
