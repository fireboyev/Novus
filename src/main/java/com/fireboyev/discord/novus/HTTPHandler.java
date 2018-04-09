package com.fireboyev.discord.novus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fireboyev.discord.novus.filestorage.FileManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import net.dv8tion.jda.core.entities.Guild;

public class HTTPHandler implements HttpHandler {
	public HTTPHandler() {

	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		String response = "404 Not Found - I think you're in the wrong place...";
		if (t.getRequestURI().toString().endsWith("insults")) {
			String uri = t.getRequestURI().toString();
			uri = uri.replace("/insults", "");
			String[] uriParts = uri.split("/");
			if (uriParts.length > 1) {
				String gid = uriParts[uriParts.length - 1];
				Guild g = Main.getJda().getGuildById(gid);
				if (g != null) {
					List<String> insults = FileManager.openGuildFolder(g).getInsults();
					response = String.join("\n", insults);
				}
			} else {
				response = "Invalid Request, You're in the wrong place.";
			}
		} else if (t.getRequestURI().toString().endsWith("compliments")) {
			String uri = t.getRequestURI().toString();
			uri = uri.replace("/compliments", "");
			String[] uriParts = uri.split("/");
			if (uriParts.length > 1) {
				String gid = uriParts[uriParts.length - 1];
				Guild g = Main.getJda().getGuildById(gid);
				if (g != null) {
					List<String> compliments = FileManager.openGuildFolder(g).getCompliments();
					response = String.join("\n", compliments);
				}
			} else {
				response = "Invalid Request, You're in the wrong place.";
			}
		}
		t.sendResponseHeaders(200, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		t.close();
	}

}
