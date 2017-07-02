package com.fireboyev.discord.selfbot.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class GuildFolder {
	File folder;
	File guildlogFile;
	File insultsFile;
	File complimentsFile;
	File configFile;

	public GuildFolder(File folder) {
		this.folder = folder;
		File insultsFile = new File(folder, "insults.novus");
		File guildlogFile = new File(folder, "guildlog.novus");
		File complimentsFile = new File(folder, "compliments.novus");
		File configFile = new File(folder, "config.novus");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	public JSONObject getJson() {
		JSONObject json = new JSONObject();
		try {
			BufferedReader br = new BufferedReader(new FileReader(getConfigFile()));
			String jsonString = br.readLine();
			if (jsonString != null) {
				if (jsonString.length() > 1) {
					json = new JSONObject(jsonString);
				}
			}
			br.close();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getCommandPrefix() {
		String prefix = ">";
		JSONObject json = getJson();
		if (json.has("prefix"))
			prefix = json.getString("prefix");
		return prefix;
	}

	public void setCommandPrefix(String prefix) {
		JSONObject json = getJson();
		json.put("prefix", prefix);
		writeJsonToFile(json, getConfigFile());
	}

	public void writeJsonToFile(JSONObject json, File file) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			writer.write(json.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
