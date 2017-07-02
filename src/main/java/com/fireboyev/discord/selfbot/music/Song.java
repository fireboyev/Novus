package com.fireboyev.discord.selfbot.music;

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
}
