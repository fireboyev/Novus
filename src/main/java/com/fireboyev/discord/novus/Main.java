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
 */
package com.fireboyev.discord.novus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.fireboyev.discord.novus.badgemanager.BadgeManager;
import com.fireboyev.discord.novus.commandmanager.CommandDescription;
import com.fireboyev.discord.novus.commandmanager.CommandListener;
import com.fireboyev.discord.novus.commandmanager.CommandManager;
import com.fireboyev.discord.novus.commands.bot.BotInfoCommand;
import com.fireboyev.discord.novus.commands.bot.ChannelSay;
import com.fireboyev.discord.novus.commands.bot.GuildsList;
import com.fireboyev.discord.novus.commands.bot.PageTestCommand;
import com.fireboyev.discord.novus.commands.bot.ThingCommand;
import com.fireboyev.discord.novus.commands.games.ChatBotCommand;
import com.fireboyev.discord.novus.commands.games.CoinCommand;
import com.fireboyev.discord.novus.commands.games.DiceCommand;
import com.fireboyev.discord.novus.commands.games.RPSCommand;
import com.fireboyev.discord.novus.commands.games.ReverseWordCommand;
import com.fireboyev.discord.novus.commands.games.compliments.AddComplimentCommand;
import com.fireboyev.discord.novus.commands.games.compliments.ComplimentCommand;
import com.fireboyev.discord.novus.commands.games.compliments.ResetComplimentsCommand;
import com.fireboyev.discord.novus.commands.games.insults.AddInsultCommand;
import com.fireboyev.discord.novus.commands.games.insults.InsultCommand;
import com.fireboyev.discord.novus.commands.games.insults.ResetInsultsCommand;
import com.fireboyev.discord.novus.commands.guild.SettingsCommand;
import com.fireboyev.discord.novus.commands.music.ForwardCommand;
import com.fireboyev.discord.novus.commands.music.PlayCommand;
import com.fireboyev.discord.novus.commands.music.PlaylistCommand;
import com.fireboyev.discord.novus.commands.music.SkipCommand;
import com.fireboyev.discord.novus.commands.user.BadgeCommand;
import com.fireboyev.discord.novus.commands.user.RemindCommand;
import com.fireboyev.discord.novus.commands.util.HelpCommand;
import com.fireboyev.discord.novus.commands.util.InviteCommand;
import com.fireboyev.discord.novus.commands.util.PurgeCommand;
import com.fireboyev.discord.novus.commands.util.SayCommand;
import com.fireboyev.discord.novus.commands.util.ServerInfoCommand;
import com.fireboyev.discord.novus.commands.util.TTSCommand;
import com.fireboyev.discord.novus.commands.util.UserCommand;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.listeners.ChatListener;
import com.fireboyev.discord.novus.listeners.EvalCommand;
import com.fireboyev.discord.novus.listeners.GuildJoinListener;
import com.fireboyev.discord.novus.listeners.ReactionListener;
import com.fireboyev.discord.novus.music.BotMusicManager;
import com.fireboyev.discord.novus.util.AniList;
import com.fireboyev.discord.novus.util.ChatBot;
import com.fireboyev.discord.novus.util.DiscordBotList;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sun.net.httpserver.HttpServer;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;

public class Main {
	private static JDA jda;
	public static BotMusicManager musicManager;
	public static CommandManager cm;
	public static BadgeManager bm;
	public static ChatBot chatBot;
	public static AniList aniList;
	public static DiscordBotList dbl;
	public static HttpServer server;
	public static EventWaiter waiter;

	public static void main(String[] args) throws IOException {
		bm = new BadgeManager();
		cm = new CommandManager();
		waiter = new EventWaiter();
		FileManager.CreateDefaultFiles();
		File folder = FileManager.getBotFolder();
		File tokenFile = new File(folder, "token.novus");
		if (!tokenFile.exists())
			tokenFile.createNewFile();
		BufferedReader reader = new BufferedReader(new FileReader(tokenFile));
		String token = reader.readLine();
		String cBToken = reader.readLine();
		String dblToken = reader.readLine();
		chatBot = new ChatBot(cBToken);
		aniList = new AniList();
		dbl = new DiscordBotList(dblToken);
		reader.close();
		if (token == null) {
			System.out.println("Token Not Found in " + tokenFile.getPath());
			System.out.println("Exiting...");
			System.exit(0);
		}
		registerCommands();
		try {
			jda = new JDABuilder(AccountType.BOT).setToken(token).setAutoReconnect(true)
					.addEventListener(new ChatListener()).addEventListener(new EvalCommand())
					.addEventListener(new CommandListener()).addEventListener(new ReactionListener())
					.addEventListener(new GuildJoinListener()).addEventListener(waiter).buildBlocking();
			musicManager = new BotMusicManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Starting HTTP Listener");
		long start = System.currentTimeMillis();
		server = HttpServer.create(new InetSocketAddress(3826), 0);
		server.setExecutor(null); // creates a default executor
		for (Guild g : jda.getGuilds()) {
			server.createContext("/guildinfo&key=novus&guild=" + g.getId(), new GuildHTTP(g.getIdLong()));
		}
		// server.start();
		System.out.println("Listener Started in " + (System.currentTimeMillis() - start) + "ms");
		int guildNum = jda.getGuilds().size();
		jda.getPresence().setGame(Game.of(GameType.WATCHING, "over " + guildNum + " Guilds"));
		dbl.updateDiscordBotLists(guildNum);
	}

	public static JDA getJda() {
		return jda;
	}

	private static void registerCommands() {
		cm.registerCommand("insult", new CommandDescription("Insult", "Insult People!", "%1Insult @User|Text"),
				new InsultCommand());
		cm.registerCommand("help",
				new CommandDescription("help", "Displays the help menu", new String[] { "?" }, "%1help"),
				new HelpCommand());
		cm.registerCommand("compliment",
				new CommandDescription("Compliment", "Compliment People!", "%1Compliment @User|Text"),
				new ComplimentCommand());
		cm.registerCommand(
				"addcompliment", new CommandDescription("AddCompliment",
						"Adds Compliments To the Guild Compliment List", "%1Addcompliment <Text>"),
				new AddComplimentCommand());
		cm.registerCommand("addinsult",
				new CommandDescription("AddInsult", "Adds Insults To the Guild Insult List", "%1Addinsult <Text>"),
				new AddInsultCommand());
		cm.registerCommand("play", new CommandDescription("Play", "Play Songs!", "%1Play <Song>"), new PlayCommand());
		cm.registerCommand("skip", new CommandDescription("Skip", "Skip the current song.", "%1Skip"),
				new SkipCommand());
		cm.registerCommand("say", new CommandDescription("Say", "Make me say stuff!", "%1Say <Text>"),
				new SayCommand());
		cm.registerCommand("tts", new CommandDescription("TTS", "Make me say text-to-speach stuff!", "%1Tts <Text>"),
				new TTSCommand());
		cm.registerCommand("coin", new CommandDescription("Coin", "Flip a Coin!", "%1Coin"), new CoinCommand());
		cm.registerCommand("dice", new CommandDescription("Dice", "Roll a die!", "%1Dice"), new DiceCommand());
		cm.registerCommand("serverinfo",
				new CommandDescription("Serverinfo", "Displays info about the server!", "%1Say <Text>"),
				new ServerInfoCommand());
		cm.registerCommand("user", new CommandDescription("User", "Display Info About a User", "%1User @User"),
				new UserCommand());
		cm.registerCommand("purge",
				new CommandDescription("Purge", "Purge Large Amounts of Messages at once.", "%1purge <2-100>"),
				new PurgeCommand());
		cm.registerCommand("rps",
				new CommandDescription("RPS", "Play Rock, Paper, Scissors With Me.", "%1rps <rock|paper|scissors>"),
				new RPSCommand());
		cm.registerCommand("playlist",
				new CommandDescription("Playlist", "Lists your favourite songs", "%1playlist <page>"),
				new PlaylistCommand());
		cm.registerCommand("settings",
				new CommandDescription("Settings", "Control the Settings for the Guild", "%1settings"),
				new SettingsCommand());
		cm.registerCommand("bot.guilds", new CommandDescription("", "", false, "", ""), new GuildsList());
		//cm.registerCommand("image", new CommandDescription("", "", false, "", ""), new ImageCommand());
		cm.registerCommand("channelsay", CommandDescription.getBlank(), new ChannelSay());
		cm.registerCommand("reverseword",
				new CommandDescription("Reverse Word", "Reverse a word... or more!", "%1reverseword <words>"),
				new ReverseWordCommand());
		cm.registerCommand("invite", new CommandDescription("Invite", "Get The Invite Link For Novus", "%1invite"),
				new InviteCommand());
		cm.registerCommand("badges", CommandDescription.getBlank(), new BadgeCommand());
		cm.registerCommand("cb", CommandDescription.getBlank(), new ChatBotCommand());
		cm.registerCommand("info", new CommandDescription("Info", "Shows The Info for Novus.", "%1info"),
				new BotInfoCommand());
		cm.registerCommand("thing", CommandDescription.getBlank(), new ThingCommand());
		cm.registerCommand(
				"forward", new CommandDescription("forward",
						"Skip the currently playing track Forward or Backward a few seconds", "%1forward <seconds>"),
				new ForwardCommand());
		cm.registerCommand(
				"resetcompliments", new CommandDescription("resetcompliments",
						"Resets all the guild compliments. Only Admins can use this command.", "%1resetcompliments"),
				new ResetComplimentsCommand());
		cm.registerCommand(
				"resetinsults", new CommandDescription("resetinsults",
						"Resets all the guild insults. Only Admins can use this command.", "%1resetinsults"),
				new ResetInsultsCommand());
		cm.registerCommand("remindme", CommandDescription.getBlank(), new RemindCommand());
		cm.registerCommand("pagetest", CommandDescription.getBlank(), new PageTestCommand());
	}

	public static BotMusicManager getMusicManager() {
		return musicManager;
	}

	public static BadgeManager getBadgeManager() {
		return bm;
	}

	public static ChatBot getChatBot() {
		return chatBot;
	}

	public static AniList getAniList() {
		return aniList;
	}

}
