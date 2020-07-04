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
 */package com.fireboyev.discord.novus.filestorage.config.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fireboyev.discord.novus.censormanager.config.CensorConfig;
import com.fireboyev.discord.novus.commandmanager.Command;

import net.dv8tion.jda.api.entities.User;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GuildOptions {
	@Setting("playlist")
	private PlaylistOptions playlist = new PlaylistOptions();
	@Setting("prefix")
	private String commandPrefix = "n!";
	@Setting("allowCIEditing")
	public boolean allowCIEditing = false;

	@Setting("joinMessage")
	public String joinMessage = "";
	@Setting("leaveMessage")
	public String leaveMessage = "";
	@Setting("censoring")
	public CensorConfig censoring = new CensorConfig();
	@Setting("joinleaveChannel")
	public Long joinLeaveChannel = null;
	@Setting("musicVoice")
	public Long musicVoiceChannel = null;
	@Setting("musicText")
	public Long musicTextChannel = null;
	@Setting("loggingChannel")
	public Long loggingChannel = null;
	@Setting("commandBans")
	public HashMap<String, List<Long>> commandBans = new HashMap<String, List<Long>>();

	public PlaylistOptions getPlaylist() {
		return playlist;
	}

	public void setPlaylist(PlaylistOptions playlist) {
		this.playlist = playlist;
	}

	public void setCommandPrefix(String commandPrefix) {
		this.commandPrefix = commandPrefix;
	}

	public void cmdBanUser(Command cmd, User user) {
		cmdBanUser(cmd.getName(), user.getIdLong());
	}

	public void cmdBanUser(String cmd, Long user) {
		List<Long> bannedUsers = commandBans.get(cmd);
		if (bannedUsers == null)
			bannedUsers = new ArrayList<Long>();
		bannedUsers.add(user);
		commandBans.put(cmd, bannedUsers);
	}

	public void cmdUnbanUser(Command cmd, User user) {
		cmdUnbanUser(cmd.getName(), user.getIdLong());
	}

	public void cmdUnbanUser(String cmd, Long user) {
		List<Long> bannedUsers = commandBans.get(cmd);
		if (bannedUsers == null)
			bannedUsers = new ArrayList<Long>();
		bannedUsers.remove(user);
		commandBans.put(cmd, bannedUsers);
	}

	public boolean isCmdBanned(Command cmd, User user) {
		return isCmdBanned(cmd.getName(), user.getIdLong());
	}

	public boolean isCmdBanned(String cmd, Long user) {
		List<Long> bannedUsers = commandBans.get(cmd);
		if (bannedUsers == null)
			bannedUsers = new ArrayList<Long>();
		return bannedUsers.contains(user);
	}

	public String getCommandPrefix() {
		return commandPrefix;
	}
}
