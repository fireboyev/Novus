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
 */package com.fireboyev.discord.novus.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fireboyev.discord.novus.Main;

public class ConsoleListener {
	public void Enable() {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String cmd = br.readLine();
			String[] args = cmd.split(" ");
			if (args[0].equalsIgnoreCase("say")) {
				if (args.length > 2) {
					String serverId = args[1];
					String message = cmd.replace("say", "").replace(serverId, "");

					System.out.println("Sending Message: " + message + " to textchannel: "
							+ Main.getJda().getTextChannelById(serverId).getGuild() + ":"
							+ Main.getJda().getTextChannelById(serverId).getName());
					Main.getJda().getTextChannelById(serverId).sendMessage(message).queue();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
