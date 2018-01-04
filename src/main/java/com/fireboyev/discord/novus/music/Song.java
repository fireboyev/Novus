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

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Song {
	@Setting("name")
	private String name;
	@Setting("id")
	private String id;
	@Setting("author")
	private String author;
	@Setting("duration")
	private long duration;

	public Song(String name, String id, String author, long duration) {
		this.name = name;
		this.id = id;
		this.author = author;
		this.duration = duration;
	}
	@SuppressWarnings("unused")
	private Song() {
		//required for hocon to work
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public long getDuration() {
		return duration;
	}

	@Override
	public boolean equals(Object paramObject) {
		if (paramObject instanceof Song) {
			Song song = (Song) paramObject;
			if (song.getId().equals(this.getId()))
				return true;
		}
		return super.equals(paramObject);
	}

	@Override
	public String toString() {
		return "Song{" + this.getName() + ", " + this.getAuthor() + ", " + this.getId() + ", " + this.getDuration()
				+ "}";
	}
}
