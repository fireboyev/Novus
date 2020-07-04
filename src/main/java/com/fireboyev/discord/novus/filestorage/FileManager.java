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
package com.fireboyev.discord.novus.filestorage;

import com.fireboyev.discord.novus.badgemanager.BadgeInfo;
import com.fireboyev.discord.novus.objects.BotFolder;
import com.fireboyev.discord.novus.objects.GuildFolder;
import com.fireboyev.discord.novus.objects.UserFolder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;

public class FileManager {
    private static File folder;
    private static File guilds;
    private static File users;
    private static File bot;
    private static HashMap<Long, GuildFolder> guildFolders;
    private static HashMap<Long, UserFolder> userFolders;
    private static File badgesListFile;
    private static HashMap<String, BadgeInfo> badges;
    private static BotFolder botFolder;

    public static File getBotFolder() {
        return folder;
    }

    public static int guildSize() {
        return guildFolders.size();
    }

    public static int userSize() {
        return userFolders.size();
    }

    public static GuildFolder openGuildFolder(Guild guild) {
        if (guildFolders.containsKey(guild.getIdLong())) {
            return guildFolders.get(guild.getIdLong());
        }
        File guildFolder = new File(guilds, guild.getId());
        guildFolder.mkdirs();
        GuildFolder folder = new GuildFolder(guildFolder);
        guildFolders.put(guild.getIdLong(), folder);
        return folder;
    }


    public static UserFolder openUserFolder(User user) {
        if (userFolders.containsKey(user.getIdLong())) {
            return userFolders.get(user.getIdLong());
        }
        File userFolder = new File(users, user.getId());
        userFolder.mkdirs();
        UserFolder folder = new UserFolder(userFolder);
        userFolders.put(user.getIdLong(), folder);
        return folder;
    }
    public static BotFolder openBotFolder(){
        if (botFolder!=null)
            return botFolder;
        botFolder = new BotFolder(bot);
        return botFolder;
    }

    public static File getMainGuildsFolder() {
        return guilds;
    }

    public static File getMainUsersFolder() {
        return users;
    }

    public static File getBadgesListFile() {
        return badgesListFile;
    }

    public static void CreateDefaultFiles() {
        File folder = new File(getProgramDirectory());
        File guilds = new File(folder, "guilds");
        File users = new File(folder, "users");
        File bot = new File(folder, "bot");
        guilds.mkdirs();
        users.mkdirs();
        bot.mkdirs();
        FileManager.folder = folder;
        FileManager.guilds = guilds;
        FileManager.users = users;
        FileManager.bot = bot;
        guildFolders = new HashMap<>();
        userFolders = new HashMap<>();
        initBadges();
    }

    private static void initBadges() {
        File badgesFolder = new File(bot, "badges");
        File badges = new File(bot, "badges.novus");
        badgesFolder.mkdirs();
        if (!badges.exists())
            try {
                badges.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        FileManager.badgesListFile = badges;
        loadBadges();
    }

    public static Collection<BadgeInfo> getAllBadges() {
        return badges.values();
    }

    private static void loadBadges() {
        long oldTime = System.currentTimeMillis();
        int errors = 0;
        int badges = 0;
        FileManager.badges = new HashMap<String, BadgeInfo>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(badgesListFile));
            File badgesFolder = new File(bot, "badges");
            String line = reader.readLine();
            while (line != null) {
                try {
                    String[] args = line.split(":");
                    File imageFile = new File(badgesFolder, args[0] + "." + args[1]);
                    BufferedImage image = ImageIO.read(imageFile.toURI().toURL());
                    FileManager.badges.put(args[0], new BadgeInfo(args[0], image, Integer.parseInt(args[2])));
                    badges++;
                } catch (Exception e) {
                    errors++;
                    e.printStackTrace();
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
        long time = System.currentTimeMillis() - oldTime;
        System.out.println(badges + " Badges loaded in " + time + "ms with " + errors + " errors.");
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
