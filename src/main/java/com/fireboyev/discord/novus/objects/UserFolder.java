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
 */package com.fireboyev.discord.novus.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fireboyev.discord.novus.filestorage.config.user.UserOptions;
import com.fireboyev.discord.novus.music.Song;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class UserFolder {
	File folder;
	File songs;
	File configFile;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode configNode;
	public UserOptions options;

	public UserFolder(File folder) {
		this.folder = folder;
		File configFile = new File(folder, "config.novus");
		this.configFile = configFile;
		File songs = new File(folder, "songs.novus");
		if (!songs.exists())
			try {
				songs.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		this.songs = songs;
		loader = HoconConfigurationLoader.builder().setFile(configFile)
				.setDefaultOptions(ConfigurationOptions.defaults().setShouldCopyDefaults(true)).build();
		setup();
	}

	public void setup() {
		try {
			this.configNode = ((CommentedConfigurationNode) this.loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		options = new UserOptions();
		if (!Files.exists(this.configFile.toPath(), new LinkOption[0])) {
			save();
		} else {
			load();
		}
	}

	public void load() {
		try {
			options = (UserOptions) this.configNode.getValue(TypeToken.of(UserOptions.class));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			this.configNode.setValue(TypeToken.of(UserOptions.class), options);
			this.loader.save(this.configNode);
		} catch (IOException | ObjectMappingException e) {
			e.printStackTrace();
		}
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

	public void RemoveAllSongs() {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getSongsFile()), "utf-8"));
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeSong(Song song) {
		List<Song> songs = getSongs();
		for (Song songTemp : songs) {
			if (song.getId().equals(songTemp.getId())) {
				songs.remove(songTemp);
				break;
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
