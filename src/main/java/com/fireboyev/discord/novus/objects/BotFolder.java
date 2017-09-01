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

import org.json.JSONException;
import org.json.JSONObject;

public class BotFolder {
	File settings;
	File folder;

	public BotFolder(File bot) {
		this.folder = bot;
		File settings = new File(folder, "botsettings.novus");
		try {
			settings.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.settings = settings;
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

	public File getConfigFile() {
		return settings;
	}
}
