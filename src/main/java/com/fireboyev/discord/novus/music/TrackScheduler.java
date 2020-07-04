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
 */package com.fireboyev.discord.novus.music;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.fireboyev.discord.novus.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class TrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player;
	private final BlockingQueue<AudioTrack> queue;
	private TextChannel channel;

	/**
	 * @param player
	 *            The audio player this scheduler uses
	 */
	public TrackScheduler(AudioPlayer player, TextChannel channel) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.channel = channel;
	}

	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track
	 *            The track to play or add to queue.
	 */
	public void queue(AudioTrack track) {
		// Calling startTrack with the noInterrupt set to true will start the
		// track only if nothing is currently playing. If
		// something is playing, it returns false and does nothing. In that case
		// the player was already playing so this
		// track goes to the queue instead.
		if (track.getInfo().uri.endsWith("openingNovus/.mp3")) {
			player.startTrack(track, false);
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nextTrack();
		} else if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public void setTextChannel(TextChannel channel) {
		this.channel = channel;
	}

	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	public void nextTrack() {
		// Start the next track, regardless of if something is already playing
		// or not. In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply
		// stop the player.
		AudioTrack at = queue.poll();
		if (at == null) {
			if (channel != null) {
				if (channel.canTalk()) {
					channel.sendMessage("**Queue Concluded**").queue();
					channel.getGuild().getAudioManager().closeAudioConnection();
				}
			}
		}
		player.startTrack(at, false);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		// Only start the next track if the end reason is suitable for it
		// (FINISHED or LOAD_FAILED)
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		if (!track.getInfo().title.equalsIgnoreCase("Unknown title"))
			if (channel != null) {
				if (channel.canTalk()) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setTitle("Now Playing:", "https://www.youtube.com/watch?v=" + track.getIdentifier());
					builder.addField("Title", track.getInfo().title, false);
					builder.addField("Author", track.getInfo().author, false);
					builder.addField("Duration", getDurationBreakdown(track.getDuration()), false);
					channel.sendMessage(builder.build()).queue(m -> {
						m.addReaction("?").queue();
						m.addReaction("?").queue();
						AudioTrackInfo info = track.getInfo();
						Main.getMusicManager().getGuildAudioPlayer(channel.getGuild()).addSong(m.getIdLong(),
								new Song(info.title, info.identifier, info.author, info.length));
					});
				}
			}
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

	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}
}
