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
 */package com.fireboyev.discord.novus.commands.games;

import java.util.Random;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RPSCommand implements GuildCommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		if (args.length != 2) {
			channel.sendMessage("That is not a valid choice! Please choose: rock, paper, scissors OR 1, 2 ,3").queue();
			return;
		}
		Random r = new Random();
		int userChoice;
		int botChoice = r.nextInt(2) + 1;
		if (args[1].equalsIgnoreCase("rock") || args[1].equalsIgnoreCase("1")) {
			userChoice = 1;
		} else if (args[1].equalsIgnoreCase("paper") || args[1].equalsIgnoreCase("2")) {
			userChoice = 2;
		} else if (args[1].equalsIgnoreCase("scissors") || args[1].equalsIgnoreCase("3")) {
			userChoice = 3;
		} else {
			channel.sendMessage("That is not a valid choice! Please choose: rock, paper, scissors OR 1, 2 ,3").queue();
			return;
		}
		channel.sendMessage("You Choose **" + getChoice(userChoice) + "**, I Choose **" + getChoice(botChoice) + "**.")
				.queue();
		if (userChoice == botChoice) {
			channel.sendMessage("It's a Tie!").queue();
		} else {
			if (userChoice == 1 && botChoice == 2) {
				channel.sendMessage("I win!").queue();
				return;
			}
			if (userChoice == 2 && botChoice == 1) {
				channel.sendMessage("You win!").queue();
				return;
			}
			if (userChoice == 1 && botChoice == 3) {
				channel.sendMessage("You win!").queue();
				return;
			}
			if (userChoice == 3 && botChoice == 1) {
				channel.sendMessage("I win!").queue();
				return;
			}
			if (userChoice == 3 && botChoice == 2) {
				channel.sendMessage("You win!").queue();
				return;
			}
			if (userChoice == 2 && botChoice == 3) {
				channel.sendMessage("I win!").queue();
				return;
			}
		}
	}

	public String getChoice(int choice) {
		if (choice == 1)
			return "Rock";
		else if (choice == 2)
			return "Paper";
		else
			return "Scissors";
	}
}
