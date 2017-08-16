package com.fireboyev.discord.novus.util;

public class ChatBot {
	public static String Chat(String message) {
		String lower = message.toLowerCase();
		String[] args = lower.split(" ");
		if (args[1].contains("are")) {
			if (args[2].contains("you")) {
				if (args[3].contains("dead")) {
					return "Possibly.";
				}
				else if (args[3].contains("smart")) {
					return "My Master is Smarter Than Me.";
				}
				else if (args[3].contains("alive")) {
					return "I'm not so sure.";
				}
			}
			else if (args[2].contains("they")) {
				if (args[3].contains("dead")){
					return "How should I know if they are dead?";
				}
				if (args[3].contains("smart")){
					return "Well, since you are asking me this question, I think that they are smarter than you.";
				}
			}
		}
		if (args[1].contains("who")){
			if (args[2].contains("is")){
				if (args[3].contains("your")){
					if (args[4].contains("master")){
						return "The great and powerful Oz aka fireboyev";
					}
				}
			}
		}
		return "I don't Understand.";
	}
}
