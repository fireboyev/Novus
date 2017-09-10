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
 */package com.fireboyev.discord.novus.commands.music;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ForwardCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		AudioPlayer player = Main.getMusicManager().getGuildAudioPlayer(guild).player;
		if (player.getPlayingTrack() != null) {
			if (args.length > 1) {
				try {
					long amount = Long.parseLong(args[1]);
					if (amount != 0) {
						Main.getMusicManager().getGuildAudioPlayer(guild).player.getPlayingTrack()
								.setPosition(player.getPlayingTrack().getPosition() + (amount * 1000));
						if (amount > 0)
							channel.sendMessage("Skipped Forward " + amount + " Second(s)").queue();
						else
							channel.sendMessage("Skipped Backward " + -amount + " Second(s)").queue();
					} else
						channel.sendMessage(
								"Hey there, I'm sorry, but currently space and time doesn't allow you to skip forward 0 seconds.")
								.queue();
				} catch (NumberFormatException e) {
					channel.sendMessage("Oi, I would prefer if it was a number!").queue();
				}
			} else {
				channel.sendMessage("How many seconds? Negative numbers work too!").queue();
			}

		}else
			channel.sendMessage("I could try making up my own music, but I wouldn't be very good at it... (There is no track playing currently!)").queue();
	}
}
