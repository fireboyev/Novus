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
 */package com.fireboyev.discord.novus.commands.music;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.music.BotMusicManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.model.SearchListResponse;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		BotMusicManager musicManager = Main.getMusicManager();
		if (guild != null) {
			if (args.length >= 2) {
				try {
					YouTube youtube;
					youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
							JacksonFactory.getDefaultInstance(), new HttpRequestInitializer() {
								public void initialize(HttpRequest request) throws IOException {
								}
							}).setApplicationName("youtube-cmdline-search-sample").build();

					Search.List search = youtube.search().list("id,snippet");
					search.setKey("AIzaSyAL0TFylufyCS2V2pMqthFJ177XLsD9Ovg");
					search.setQ(event.getMessage().getStrippedContent().replace(args[0], ""));
					search.setType("video");
					SearchListResponse searchResponse = search.execute();
					if (searchResponse.getItems().size() > 0) {
						musicManager.loadAndPlay(event.getTextChannel(), "https://www.youtube.com/watch?v="
								+ searchResponse.getItems().get(0).getId().getVideoId());
					} else {
						channel.sendMessage("Error, Could not find a YouTube Video by that name!").queue();
					}
				} catch (GeneralSecurityException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
		}
	}
}
