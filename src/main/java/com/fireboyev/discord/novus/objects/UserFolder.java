package com.fireboyev.discord.novus.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
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
		List<Song> songs = new ArrayList<Song>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getSongsFile()));
			String line = br.readLine();
			while (line != null) {
				String[] args = line.split(":");
				songs.add(new Song(args[0], args[1], args[2], Long.parseLong(args[3])));
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return songs;
	}

	public void addSong(Song song) {
		List<Song> songs = getSongs();
		songs.add(song);
		String songsList = "";
		for (Song s : songs) {
			songsList += s.getName() + ":" + s.getId() + ":" + s.getAuthor() + ":" + s.getDuration()
					+ System.lineSeparator();
		}
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getSongsFile()), "utf-8"));
			writer.write(songsList);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void removeSong(Song song) {
		List<Song> songs = getSongs();
		for (Song songTemp : songs) {
			if (song.getId() == songTemp.getId()) {
				songs.remove(songTemp);
			}
		}
		String songsList = "";
		for (Song s : songs) {
			songsList += s.getName() + ":" + s.getId() + ":" + s.getAuthor() + ":" + s.getDuration()
					+ System.lineSeparator();
		}
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getSongsFile()), "utf-8"));
			writer.write(songsList);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addReminder(String reminder, Calendar date) {

	}

}
