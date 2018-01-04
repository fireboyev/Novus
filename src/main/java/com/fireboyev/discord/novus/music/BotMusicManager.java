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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;

public class BotMusicManager {
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;

	public BotMusicManager() {
		this.musicManagers = new HashMap<>();
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	public synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			if (guild.getTextChannelsByName("music", true).size() > 0)
				musicManager = new GuildMusicManager(playerManager, guild.getTextChannelsByName("music", true).get(0));
			else if (guild.getTextChannelsByName("spam", true).size() > 0)
				musicManager = new GuildMusicManager(playerManager, guild.getTextChannelsByName("spam", true).get(0));
			else
				musicManager = new GuildMusicManager(playerManager, null);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}

	public List<AudioTrack> getQueue(TextChannel channel) {
		List<AudioTrack> list = new ArrayList<AudioTrack>();
		list.addAll(getGuildAudioPlayer(channel.getGuild()).scheduler.getQueue());
		return list;
	}

	public void loadAndPlay(final TextChannel channel, final String trackUrl) {
		final GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

				play(channel.getGuild(), musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist "
						+ playlist.getName() + ")").queue();

				play(channel.getGuild(), musicManager, firstTrack);
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Nothing found by " + trackUrl).queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}

	private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
		connectToMusicChannel(guild.getAudioManager());
		musicManager.scheduler.queue(track);
	}

	public void skipTrack(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();

		channel.sendMessage("Skipped to next track.").queue();
	}

	private void connectToMusicChannel(AudioManager audioManager) {
		if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
			if (audioManager.getGuild().getVoiceChannelsByName("Music", true).size() > 0) {
				audioManager.openAudioConnection(audioManager.getGuild().getVoiceChannelsByName("Music", true).get(0));
				return;
			}
			for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
				audioManager.openAudioConnection(voiceChannel);
				break;
			}
		}
	}
}
