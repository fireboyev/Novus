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
package com.fireboyev.discord.novus.commands.bot;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThingCommand implements GuildCommandExecutor {

    @Override
    public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel, MessageReceivedEvent event) {
        channel.sendMessage("Deleting All Voice and Text Channels. Please wait...").queue();
        for (GuildChannel tc : guild.getChannels()) {
            if (tc.getIdLong() != channel.getIdLong())
                tc.delete().queue();
        }
        for (VoiceChannel vc : guild.getVoiceChannels()) {
            vc.delete().queue();
        }
    }
}
