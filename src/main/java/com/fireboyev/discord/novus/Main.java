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

import com.fireboyev.discord.novus.badgemanager.BadgeManager;
import com.fireboyev.discord.novus.censormanager.config.CensorUtil;
import com.fireboyev.discord.novus.commandmanager.CommandDescription;
import com.fireboyev.discord.novus.commandmanager.CommandListener;
import com.fireboyev.discord.novus.commandmanager.CommandManager;
import com.fireboyev.discord.novus.commands.bot.*;
import com.fireboyev.discord.novus.commands.games.*;
import com.fireboyev.discord.novus.commands.games.compliments.AddComplimentCommand;
import com.fireboyev.discord.novus.commands.games.compliments.ComplimentCommand;
import com.fireboyev.discord.novus.commands.games.compliments.ResetComplimentsCommand;
import com.fireboyev.discord.novus.commands.games.insults.AddInsultCommand;
import com.fireboyev.discord.novus.commands.games.insults.InsultCommand;
import com.fireboyev.discord.novus.commands.games.insults.ResetInsultsCommand;
import com.fireboyev.discord.novus.commands.guild.FunctionBanCommand;
import com.fireboyev.discord.novus.commands.guild.FunctionUnbanCommand;
import com.fireboyev.discord.novus.commands.guild.JoinLeaveCommand;
import com.fireboyev.discord.novus.commands.guild.SettingsCommand;
import com.fireboyev.discord.novus.commands.guild.censoring.CensorConfigCommand;
import com.fireboyev.discord.novus.commands.image.*;
import com.fireboyev.discord.novus.commands.music.*;
import com.fireboyev.discord.novus.commands.user.BadgeCommand;
import com.fireboyev.discord.novus.commands.user.RemindCommand;
import com.fireboyev.discord.novus.commands.util.*;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.filestorage.config.guild.GuildOptions;
import com.fireboyev.discord.novus.listeners.ChatListener;
import com.fireboyev.discord.novus.listeners.EvalCommand;
import com.fireboyev.discord.novus.listeners.GuildJoinListener;
import com.fireboyev.discord.novus.listeners.ReactionListener;
import com.fireboyev.discord.novus.music.BotMusicManager;
import com.fireboyev.discord.novus.util.AniList;
import com.fireboyev.discord.novus.util.ChatBot;
import com.fireboyev.discord.novus.util.DiscordBotList;
import com.fireboyev.discord.novus.util.Questions20Util;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sun.net.httpserver.HttpServer;
import ez.DB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.discordbots.api.client.DiscordBotListAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    public static Logger logger;
    public static BotMusicManager musicManager;
    public static CommandManager cm;
    public static BadgeManager bm;
    public static ChatBot chatBot;
    public static AniList aniList;
    public static DiscordBotList dbl;
    public static HttpServer server;
    public static EventWaiter waiter;
    public static CensorUtil censoring;
    public static DiscordBotListAPI dbla2;
    public static Questions20Util q20util;
    public static DB database;
    private static JDA jda;

    public static void main(String[] args) throws IOException {
        long totalMilli = System.currentTimeMillis();
        logger = Logger.getLogger("Novus");
        System.out.println("Starting Novus...");
        System.out.println("Initializing Managers and Utils...");
        bm = new BadgeManager();
        cm = new CommandManager();
        waiter = new EventWaiter();
        censoring = new CensorUtil();
        q20util = new Questions20Util();
        System.out.println("Created Default Files");

        FileManager.CreateDefaultFiles();
        File folder = FileManager.getBotFolder();
        File tokenFile = new File(folder, "token.novus");
        if (!tokenFile.exists())
            tokenFile.createNewFile();
        BufferedReader reader = new BufferedReader(new FileReader(tokenFile));
        String token = reader.readLine();
        String cBToken = reader.readLine();
        String dblToken = reader.readLine();
        String dbl2Token = reader.readLine();
        String[] sqlAuth = reader.readLine().split(":");
        reader.close();
        database = new DB(sqlAuth[0], sqlAuth[1], sqlAuth[2], sqlAuth[3]);
        initSQL();
        System.out.println("Initializing Third Party APIs...");
        chatBot = new ChatBot(cBToken);
        aniList = new AniList();
        dbl = new DiscordBotList(dblToken);
        dbla2 = new DiscordBotListAPI.Builder().token(dbl2Token).build();
        if (token == null) {
            System.out.println("Token Not Found in " + tokenFile.getPath());
            System.out.println("Exiting...");
            System.exit(0);
        }
        q20util.LoadProfiles();
        System.out.println("Registering Commands...");
        registerCommands();
        try {
            System.out.println("Starting JDA Instance...");
            long jdaMillis = System.currentTimeMillis();
            jda = JDABuilder.createDefault(token)
                    .setAutoReconnect(true)
                    .addEventListeners(new ChatListener(), new EvalCommand())
                    .addEventListeners(new CommandListener()).addEventListeners(new ReactionListener())
                    .addEventListeners(new GuildJoinListener()).addEventListeners(waiter).build();
            musicManager = new BotMusicManager();
            System.out
                    .println("JDA Instance Started in " + Long.toString(System.currentTimeMillis() - jdaMillis) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Setting Playing Status...");
        int guildNum = jda.getGuilds().size();
        jda.getPresence().setActivity(Activity.watching("over " + guildNum + " Guilds"));
        System.out.println("Updating the Discord Bot Lists...");
        if (jda.getGuilds().size() > 10) {
            dbl.updateDiscordBotLists(guildNum);
            dbla2.setStats(jda.getSelfUser().getId(), jda.getGuilds().size());
        }
        //server = HttpServer.create(new InetSocketAddress(8080), 0);
        //server.setExecutor(null);
        //server.createContext("/", new HTTPHandler());
        //server.start();
        System.out
                .println("Novus Startup Completed in " + Long.toString(System.currentTimeMillis() - totalMilli) + "ms");
    }

    public static void initSQL() {
        GuildOptions.initSQL(database);
    }

    public static JDA getJda() {
        return jda;
    }

    private static void registerCommands() {
        cm.registerCommand("insult", new CommandDescription("Insult", "Insult People!", "%1Insult @User|Text"),
                new InsultCommand());
        cm.registerCommand("help",
                new CommandDescription("help", "Displays the help menu", new String[]{"?"}, "%1help"),
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
        // cm.registerCommand("image", new CommandDescription("", "", false, "", ""),
        // new ImageCommand());
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
        cm.registerCommand("resetcompliments",
                new CommandDescription("resetcompliments",
                        "Resets all the guild compliments. Only Admins can use this command.", "%1resetcompliments"),
                new ResetComplimentsCommand());
        cm.registerCommand("resetinsults",
                new CommandDescription("resetinsults",
                        "Resets all the guild insults. Only Admins can use this command.", "%1resetinsults"),
                new ResetInsultsCommand());
        cm.registerCommand("remindme", CommandDescription.getBlank(), new RemindCommand());
        cm.registerCommand("pagetest", CommandDescription.getBlank(), new PageTestCommand());
        cm.registerCommand("joinleave", CommandDescription.getBlank(), new JoinLeaveCommand());
        cm.registerCommand("rate", new CommandDescription("Rate", "Rates the specified message", "%1rate <message>"),
                new RateCommand());
        cm.registerCommand("censorconfig",
                new CommandDescription("CensorConfig", "The Config Command for Censoring", "%1censorconfig"),
                new CensorConfigCommand());
        cm.registerCommand("playing",
                new CommandDescription("Playing", "Shows info about the currently playing track.", "%1playing"),
                new PlayingCommand());
        cm.registerCommand("leave", new CommandDescription("Leave", "Forces Me to Leave the Voice Channel.", "%1leave"),
                new LeaveCommand());
        cm.registerCommand("queue", CommandDescription.getBlank(), new QueueCommand());
        cm.registerCommand(
                "functionban", new CommandDescription("FunctionBan",
                        "Allows admins to ban users from using certain commands", "%1functionban <Command> <@User>"),
                new FunctionBanCommand());
        cm.registerCommand("functionunban", new CommandDescription("FunctionUnban",
                        "Allows admins to unban users from using certain commands", "%1functionunban <Command> <@User>"),
                new FunctionUnbanCommand());
        cm.registerCommand("invert", CommandDescription.getBlank(), new InvertCommand());
        // cm.registerCommand("blur", CommandDescription.getBlank(), new BlurCommand());
        cm.registerCommand("circle", CommandDescription.getBlank(), new CircleCommand());
        cm.registerCommand("ripple", CommandDescription.getBlank(), new RippleCommand());
        cm.registerCommand("twist", CommandDescription.getBlank(), new TwistCommand());
        cm.registerCommand("oil", CommandDescription.getBlank(), new OilCommand());
        //cm.registerCommand("vectorize", CommandDescription.getBlank(), new VectorizeCommand());
        cm.registerCommand("qb", CommandDescription.getBlank(), new QuestionAttributeBuilderCommand());
        cm.registerCommand("qc", CommandDescription.getBlank(), new Questions20Command());
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

    public static Logger getLogger() {
        return logger;
    }
}
