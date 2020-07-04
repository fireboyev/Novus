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
package com.fireboyev.discord.novus.util;

import com.fireboyev.discord.novus.commandmanager.CommandDescription;
import com.fireboyev.discord.novus.filestorage.FileManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;
import java.util.List;

public class Bot {
    static List<CommandDescription> commands = new ArrayList<CommandDescription>();

    public static void registerCommand(CommandDescription command) {
        commands.add(command);
    }

    public static List<CommandDescription> getCommands() {
        return commands;
    }

    public static boolean IsFire(Member member) {
        return IsFire(member.getUser());
    }

    public static boolean IsFire(User user) {
        return user.getId().equals("223230587157217280");
    }

    public static boolean IsAdmin(Member user) {
        if (user.hasPermission(Permission.ADMINISTRATOR))
            return true;
        if (user.hasPermission(Permission.MANAGE_SERVER))
            return true;
        if (user.getUser().getId().equals("223230587157217280"))
            return true;
        if (user.isOwner())
            return true;
        for (Role role : user.getRoles()) {
            if (role.getName().equals("NovusAdmin")) {
                return true;
            }
        }
        return false;
    }

    public static String getPrefix(MessageChannel mc) {
        if (mc.getType().isGuild()) {
            TextChannel tc = (TextChannel) mc;
            return FileManager.openGuildFolder(tc.getGuild()).getCommandPrefix();
        } else return "n!";
    }
}
