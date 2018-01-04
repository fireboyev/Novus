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

import java.io.File;
import java.io.IOException;
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
	File configFile;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode configNode;
	public UserOptions options;

	public UserFolder(File folder) {
		this.folder = folder;
		File configFile = new File(folder, "config.novus");
		this.configFile = configFile;
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

	public List<Song> getSongs() {
		return options.getSongs();
	}

	public void addSong(Song song) {
		options.songs.add(song);
		save();
	}

	public void RemoveAllSongs() {
		options.songs = new ArrayList<Song>();
	}

	public void removeSong(Song song) {
		options.songs.remove(song);
		save();
	}

	public void addReminder(String reminder, Calendar date) {

	}

}
