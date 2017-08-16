package com.fireboyev.discord.novus.filestorage.config.guild;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GuildOptions {
	@Setting("playlist")
	private PlaylistOptions playlist = new PlaylistOptions();
	@Setting("prefix")
	private String commandPrefix = ">";

	public PlaylistOptions getPlaylist() {
		return playlist;
	}

	public void setPlaylist(PlaylistOptions playlist) {
		this.playlist = playlist;
	}

	public void setCommandPrefix(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}

	public String getCommandPrefix() {
		return commandPrefix;
	}
}
