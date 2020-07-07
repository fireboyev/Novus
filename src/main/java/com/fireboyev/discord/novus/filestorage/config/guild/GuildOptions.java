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
package com.fireboyev.discord.novus.filestorage.config.guild;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.censormanager.config.CensorConfig;
import com.fireboyev.discord.novus.commandmanager.Command;
import ez.DB;
import ez.Row;
import ez.Table;
import net.dv8tion.jda.api.entities.User;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ConfigSerializable
public class GuildOptions {
    public Long id;
    @Setting("allowCIEditing")
    public boolean allowCIEditing = false;
    //@Setting("joinMessage")
    public String joinMessage = "";
    //@Setting("leaveMessage")
    public String leaveMessage = "";
    @Setting("censoring")
    public CensorConfig censoring = new CensorConfig();
    //@Setting("joinleaveChannel")
    public Long joinLeaveChannel = null;
    //@Setting("musicVoice")
    public Long musicVoiceChannel = null;
   // @Setting("musicText")
    public Long musicTextChannel = null;
    @Setting("loggingChannel")
    public Long loggingChannel = null;
    @Setting("commandBans")
    public HashMap<String, List<Long>> commandBans = new HashMap<String, List<Long>>();
    Row row;
    private DB database;
    @Setting("playlist")
    private PlaylistOptions playlist = new PlaylistOptions();
    @Setting("prefix")
    private String commandPrefix = "n!";

    public GuildOptions(Long id, DB database) {
        this.id = id;
        this.database = database;
        row = database.selectSingleRow("SELECT * FROM GuildConfig WHERE ID = ?", id);
        if (row == null) {
            row = new Row().with("ID", id);
            save();
        }

        this.commandPrefix = row.get("PREFIX");
        this.joinMessage = row.get("JOINMSG");
        this.leaveMessage = row.get("LEAVEMSG");
        this.joinLeaveChannel = row.getLong("JOINLEAVCHNL");
        this.musicVoiceChannel = row.getLong("MUSICVOICE");
        this.musicTextChannel = row.getLong("MUSICTEXT");
        this.loggingChannel = row.getLong("LOGGING");


    }

    // Table Layout
    public static void initSQL(DB database) {
        Table table = new Table("GuildConfig");
        if (!database.hasTable(table.name)) {
            table.primary("ID", Long.class);
            table.column("NAME", String.class);
            table.column("PREFIX", String.class);
            table.column("JOINMSG", String.class);
            table.column("LEAVEMSG", String.class);
            table.column("JOINLEAVCHNL", Long.class);
            table.column("MUSICVOICE", Long.class);
            table.column("MUSICTEXT", Long.class);
            table.column("LOGGING", Long.class);
            database.addTable(table);
        }

        Table perms = new Table("GuildPermissions");
        if (!database.hasTable(perms.name)) {
            perms.primary("ID", Long.class);
            perms.column("ROLE", Long.class);
            perms.column("PERMISSIONS", String.class);
            database.addTable(perms);
        }
    }

    public void save() {
        row.with("NAME", Main.getJda().getGuildById(id).getName());
        row.with("PREFIX", commandPrefix);
        row.with("JOINMSG", joinMessage);
        row.with("LEAVEMSG", leaveMessage);
        row.with("JOINLEAVCHNL", joinLeaveChannel);
        row.with("MUSICVOICE", musicVoiceChannel);
        row.with("MUSICTEXT", musicTextChannel);
        row.with("LOGGING", loggingChannel);
        database.replace("GuildConfig", row);
    }

    public PlaylistOptions getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlaylistOptions playlist) {
        this.playlist = playlist;
    }

    public void cmdBanUser(Command cmd, User user) {
        cmdBanUser(cmd.getName(), user.getIdLong());
    }

    public void cmdBanUser(String cmd, Long user) {
        List<Long> bannedUsers = commandBans.get(cmd);
        if (bannedUsers == null)
            bannedUsers = new ArrayList<Long>();
        bannedUsers.add(user);
        commandBans.put(cmd, bannedUsers);
    }

    public void cmdUnbanUser(Command cmd, User user) {
        cmdUnbanUser(cmd.getName(), user.getIdLong());
    }

    public void cmdUnbanUser(String cmd, Long user) {
        List<Long> bannedUsers = commandBans.get(cmd);
        if (bannedUsers == null)
            bannedUsers = new ArrayList<Long>();
        bannedUsers.remove(user);
        commandBans.put(cmd, bannedUsers);
    }

    public boolean isCmdBanned(Command cmd, User user) {
        return isCmdBanned(cmd.getName(), user.getIdLong());
    }

    public boolean isCmdBanned(String cmd, Long user) {
        List<Long> bannedUsers = commandBans.get(cmd);
        if (bannedUsers == null)
            bannedUsers = new ArrayList<Long>();
        return bannedUsers.contains(user);
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public GuildOptions setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
        return this;
    }
}
