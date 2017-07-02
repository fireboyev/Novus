package com.fireboyev.discord.novus.music;

public class Song {
	String name;
	String id;
	String author;
	long duration;

	public Song(String name, String id, String author, long duration) {
		this.name = name;
		this.id = id;
		this.author = author;
		this.duration = duration;
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
}
