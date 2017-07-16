package com.fireboyev.discord.novus.music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.entities.TextChannel;

public class GuildMusicManager {
	public final AudioPlayer player;

	private Map<Long, Song> guildSongs;
	private int downVote = 0;

	/**
	 * Track scheduler for the player.
	 */
	public final TrackScheduler scheduler;

	/**
	 * Creates a player and a track scheduler.
	 * 
	 * @param manager
	 *            Audio player manager to use for creating the player.
	 */
	public GuildMusicManager(AudioPlayerManager manager, TextChannel channel) {
		player = manager.createPlayer();
		guildSongs = new HashMap<>();
		scheduler = new TrackScheduler(player, channel);
		player.addListener(scheduler);
	}

	public int getDownVotes() {
		return downVote;
	}

	public void setDownVotes(int num) {
		this.downVote = num;
	}

	public void resetDownVote() {
		this.downVote = 0;
	}

	public Song getSong(long id) {
		return guildSongs.get(id);
	}

	public void addSong(long id, Song song) {
		guildSongs.put(id, song);
	}

	/**
	 * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
	 */
	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player);
	}
}
