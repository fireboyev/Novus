package com.fireboyev.discord.novus.objects;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.fireboyev.discord.novus.music.Song;

public class UserFolder {
	File folder;
	File songs;

	public UserFolder(File folder) {
		this.folder = folder;
		File songs = new File(folder, "songs.novus");
		if (!songs.exists())
			try {
				songs.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		this.songs = songs;
	}

	public File getFolder() {
		return folder;
	}

	public File getSongsFile() {
		return songs;
	}

	public List<Song> getSongs() {
		return null;
	}

	public void addReminder(String reminder, Calendar date) {

	}

}
