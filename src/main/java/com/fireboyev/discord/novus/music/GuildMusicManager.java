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
