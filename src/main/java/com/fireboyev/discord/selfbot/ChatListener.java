package com.fireboyev.discord.selfbot;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChatListener extends ListenerAdapter {
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getRawContent().split(" ");
		System.out.println("{" + event.getGuild().getName() + ":" + event.getChannel().getName() + "}<"
				+ event.getAuthor().getName() + "> " + event.getMessage().getContent());
		if (!event.getAuthor().isBot()) {
			if (event.getMessage().getContent().equalsIgnoreCase(">shutdown")) {
				if (event.getAuthor().getId().equals("223230587157217280")) {
					event.getChannel().sendMessage("am sorri I'll go now ;-;").queue();
					try {
						Thread.sleep(2000L);
						Main.getJda().shutdown();
					} catch (InterruptedException e) {
						event.getChannel().sendMessage("Error Shutting Down: " + e.getMessage()).queue();
					}
				} else {
					event.getChannel().sendMessage("Only My Master Can Do That!");
				}
			}
			if (event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser())) {
				if (!event.getMessage().getMentionedUsers().contains(event.getGuild().getPublicRole())) {
					event.getChannel()
							.sendMessage("Use **" + FileManager.openGuildFolder(event.getGuild()).getCommandPrefix()
									+ "** to use my commands!")
							.queue();
				}
			}
			if (args[0].equalsIgnoreCase(">createdefaultfiles")) {
				if (Bot.IsFire(event.getMember())) {
					event.getChannel().sendMessage("Generating Files...").queue();
					long oldsysTime = System.currentTimeMillis();
					FileManager.openGuildFolder(event.getGuild());
					for (Member member : event.getGuild().getMembers()) {
						FileManager.openUserFolder(member.getUser());
					}
					long currentsysTime = System.currentTimeMillis();
					event.getChannel()
							.sendMessage("Files Generated in " + Long.toString(currentsysTime - oldsysTime) + " ms")
							.queue();

				} else {
					event.getChannel().sendMessage("Sorry, You Don't Have Permission To Do This.").queue();
				}
			}
			if (args[0].equalsIgnoreCase(">folder")) {
				if (Bot.IsFire(event.getMember())) {
					event.getChannel()
							.sendMessage("Bot Folder is Located in: " + FileManager.getBotFolder().getAbsolutePath())
							.queue();
				}
			}
		}
	}
}
