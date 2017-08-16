package com.fireboyev.discord.novus.listeners;

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
