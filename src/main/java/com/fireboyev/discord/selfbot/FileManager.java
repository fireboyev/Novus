package com.fireboyev.discord.selfbot;

import java.io.File;
import java.net.URISyntaxException;

import com.fireboyev.discord.selfbot.objects.GuildFolder;
import com.fireboyev.discord.selfbot.objects.UserFolder;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class FileManager {
	private static File folder;
	private static File guilds;
	private static File users;

	public static File getBotFolder() {
		return folder;
	}

	public static GuildFolder openGuildFolder(Guild guild) {
		File guildFolder = new File(guilds, guild.getId());
		guildFolder.mkdirs();
		return new GuildFolder(guildFolder);
	}

	public static UserFolder openUserFolder(User user) {
		File userFolder = new File(users, user.getId());
		userFolder.mkdirs();
		return new UserFolder(userFolder);
	}

	public static File getMainGuildsFolder() {
		return guilds;
	}

	public static File getMainUsersFolder() {
		return users;
	}

	public static void CreateDefaultFiles() {
		File folder = new File(getProgramDirectory());
		File guilds = new File(folder, "guilds");
		File users = new File(folder, "users");
		guilds.mkdirs();
		users.mkdirs();
		FileManager.folder = folder;
		FileManager.guilds = guilds;
		FileManager.users = users;
	}

	private static String getJarName() {
		return new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
	}

	private static boolean runningFromJAR() {
		String jarName = getJarName();
		return jarName.contains(".jar");
	}

	public static String getProgramDirectory() {
		if (runningFromJAR()) {
			return getCurrentJARDirectory();
		} else {
			return getCurrentProjectDirectory();
		}
	}

	private static String getCurrentProjectDirectory() {
		return new File("").getAbsolutePath();
	}

	private static String getCurrentJARDirectory() {
		try {
			return new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
					.getParent();
		} catch (URISyntaxException exception) {
			exception.printStackTrace();
		}

		return null;
	}
}
