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
 */package com.fireboyev.discord.novus.filestorage.config.user;

import java.util.ArrayList;
import java.util.List;

import com.fireboyev.discord.novus.music.Song;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class UserOptions {
	@Setting("tokens")
	private int tokens = 200;
	@Setting("badges")
	private BadgeOptions badges = new BadgeOptions();
	@Setting("songs")
	public List<Song> songs = new ArrayList<Song>();

	public int getTokens() {
		return tokens;
	}

	public BadgeOptions getBadges() {
		return badges;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	public List<Song> getSongs() {
		return songs;
	}


}
