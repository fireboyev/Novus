package com.fireboyev.discord.novus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import org.json.JSONObject;

public class DiscordBotList {
	String token;

	public DiscordBotList(String token) {
		this.token = token;
	}

	public void updateDiscordBotLists(int guildsNum) {
		try {
			if (1==1)
				return;
			URI uri = new URI("https://bots.discord.pw/api/bots/283418267408662529/stats");
			//HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(uri);
			post.addHeader("Authorization", token);
			JSONObject json = new JSONObject();
			json.append("server_count", guildsNum);
			StringEntity params = new StringEntity(json.toString());
			params.setContentType("application/json");
			params.setChunked(true);
			post.setEntity(params);

			/*HttpResponse response = client.execute(post);
			if (response == null)
				return;
			if (response.getEntity() == null)
				return;
			if (response.getEntity().getContent() == null)
				return;
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result.toString());*/
		} catch (IOException |

				URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
