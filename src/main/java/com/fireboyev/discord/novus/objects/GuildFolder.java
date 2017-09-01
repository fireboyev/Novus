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
import java.util.List;

import com.fireboyev.discord.novus.filestorage.config.guild.GuildOptions;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class GuildFolder {
	File folder;
	File guildlogFile;
	File insultsFile;
	File complimentsFile;
	File configFile;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode configNode;
	public GuildOptions options;

	public GuildFolder(File folder) {
		this.folder = folder;
		File insultsFile = new File(folder, "insults.novus");
		File guildlogFile = new File(folder, "guildlog.novus");
		File complimentsFile = new File(folder, "compliments.novus");
		File configFile = new File(folder, "config.novus");
		if (!insultsFile.exists()) {
			try {
				insultsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!complimentsFile.exists()) {
			try {
				complimentsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!guildlogFile.exists())
			try {
				guildlogFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		this.configFile = configFile;
		this.insultsFile = insultsFile;
		this.guildlogFile = guildlogFile;
		this.complimentsFile = complimentsFile;
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
		options = new GuildOptions();
		if (!Files.exists(this.configFile.toPath(), new LinkOption[0])) {
			save();
		} else {
			load();
		}
	}

	public void load() {
		try {
			options = (GuildOptions) this.configNode.getValue(TypeToken.of(GuildOptions.class));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			this.configNode.setValue(TypeToken.of(GuildOptions.class), options);
			this.loader.save(this.configNode);
		} catch (IOException | ObjectMappingException e) {
			e.printStackTrace();
		}
	}

	public File getGuildFolder() {
		return folder;
	}

	public File getLogFile() {
		return guildlogFile;
	}

	public File getConfigFile() {
		return configFile;
	}

	public String getCommandPrefix() {
		if (options == null)
			return ">";
		if (options.getCommandPrefix() == null)
			return ">";
		return options.getCommandPrefix();
	}

	public void setCommandPrefix(String prefix) {
		options.setCommandPrefix(prefix);
		save();
	}

	public List<String> getLog() {
		List<String> log = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getLogFile()));
			String line = br.readLine();
			while (line != null) {
				log.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return log;
	}

	public void addInsult(String insult) {
		List<String> insults = getInsults();
		insults.add(insult);
		String insultListRaw = "";
		for (String st : insults) {
			insultListRaw += st + System.lineSeparator();
		}
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(getInsultsFile()), "utf-8"))) {
			writer.write(insultListRaw);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addCompliment(String compliment) {
		List<String> compliments = getCompliments();
		compliments.add(compliment);
		String complimentListRaw = "";
		for (String st : compliments) {
			complimentListRaw += st + System.lineSeparator();
		}
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(getComplimentsFile()), "utf-8"))) {
			writer.write(complimentListRaw);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getInsults() {
		List<String> insults = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getInsultsFile()));
			String line = br.readLine();
			while (line != null) {
				insults.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return insults;
	}

	public List<String> getCompliments() {
		List<String> compliments = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getComplimentsFile()));
			String line = br.readLine();
			while (line != null) {
				compliments.add(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compliments;
	}

	public File getInsultsFile() {
		return insultsFile;
	}

	public File getComplimentsFile() {
		return complimentsFile;
	}
}
