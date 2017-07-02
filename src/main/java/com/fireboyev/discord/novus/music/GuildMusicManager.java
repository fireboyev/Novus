package com.fireboyev.discord.novus.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.entities.TextChannel;

public class GuildMusicManager {
	  public final AudioPlayer player;
	  /**
	   * Track scheduler for the player.
	   */
	  public final TrackScheduler scheduler;

	  /**
	   * Creates a player and a track scheduler.
	   * @param manager Audio player manager to use for creating the player.
	   */
	  public GuildMusicManager(AudioPlayerManager manager, TextChannel channel) {
	    player = manager.createPlayer();
	    scheduler = new TrackScheduler(player, channel);
	    player.addListener(scheduler);
	  }

	  /**
	   * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
	   */
	  public AudioPlayerSendHandler getSendHandler() {
	    return new AudioPlayerSendHandler(player);
	  }
}
