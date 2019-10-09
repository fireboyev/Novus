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

import com.fireboyev.discord.novus.filestorage.config.user.UserOptions;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fireboyev.discord.novus.filestorage.config.bot.BotOptions;

public class BotFolder {
	File folder;
	File configFile;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode configNode;
	public BotOptions options;

	public BotFolder(File folder) {
		this.folder = folder;
		File configFile = new File(folder, "bot.novus");
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
		options = new BotOptions();
		if (!Files.exists(this.configFile.toPath(), new LinkOption[0])) {
			save();
		} else {
			load();
		}
	}

	public void load() {
		try {
			options = (BotOptions) this.configNode.getValue(TypeToken.of(BotOptions.class));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			this.configNode.setValue(TypeToken.of(BotOptions.class), options);
			this.loader.save(this.configNode);
		} catch (IOException | ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	public File getFolder() {
		return folder;
	}

}
