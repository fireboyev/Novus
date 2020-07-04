package com.fireboyev.discord.novus;

import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class GuildHTTP implements HttpHandler {
	long guildID;

	public GuildHTTP(long guildID) {
		this.guildID = guildID;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		JSONObject json = new JSONObject();
		json.append("guildID", guildID);
		json.append("name", Main.getJda().getGuildById(guildID).getName());
		json.append("owner", Main.getJda().getGuildById(guildID).getOwner().getUser().getName() + "#"
				+ Main.getJda().getGuildById(guildID).getOwner().getUser().getDiscriminator());
		JSONArray members = new JSONArray();
		for (Member m : Main.getJda().getGuildById(guildID).getMembers())
			members.put(m.getUser().getName() + "#" + m.getUser().getDiscriminator());
		json.append("members", members);
		JSONArray roles = new JSONArray();
		for (Role r : Main.getJda().getGuildById(guildID).getRoles())
			roles.put(r.getName());
		json.append("roles", roles);
		json.append("creation-date", Main.getJda().getGuildById(guildID).getTimeCreated());
		String response = json.toString();
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

}
