package com.fireboyev.discord.selfbot;

import com.fireboyev.discord.selfbot.objects.UserFolder;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		User user = event.getUser();
		UserFolder folder = FileManager.openUserFolder(user);
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		User user = event.getUser();
		UserFolder folder = FileManager.openUserFolder(user);
	}
}
