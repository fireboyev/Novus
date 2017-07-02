package com.fireboyev.discord.selfbot.commands.music;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.fireboyev.discord.selfbot.Main;
import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;
import com.fireboyev.discord.selfbot.music.BotMusicManager;
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
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class PlayCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
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
						musicManager.loadAndPlay(event.getChannel(), "https://www.youtube.com/watch?v="
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
