package com.fireboyev.discord.selfbot.commands.games;

import java.util.Random;

import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class RPSCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
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
