package com.fireboyev.discord.novus;

import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		event.getJDA().getPresence()
				.setGame(new GameImpl(event.getJDA().getGuilds().size() + " Guilds", null, GameType.TWITCH));
	}
}
