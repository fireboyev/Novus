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
 */package com.fireboyev.discord.novus.commands.games.insults;

import java.util.List;
import java.util.Random;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InsultCommand implements GuildCommandExecutor {
@Override
public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
		MessageReceivedEvent event) {
	if (args.length > 1) {
		StringBuilder builder = new StringBuilder(event.getMessage().getContentRaw());
		builder.delete(0, args[0].length());
		Random rand = new Random();
		List<String> insults = FileManager.openGuildFolder(guild).getInsults();
		if (insults.size() > 0) {
			String insult = insults.get(rand.nextInt(insults.size()));
			channel.sendMessage(builder.toString() + "," + insult).queue();
		} else {
			channel.sendMessage("Guild Insult List Is Empty!").queue();
		}
	} else {
		channel.sendMessage("Who should I insult?").queue();
	}
}
}
