package com.fireboyev.discord.novus.commands.games;

import java.util.Date;
import java.util.List;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.util.Formatter;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RateCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		String msg = message.getContentRaw().substring(args[0].length() + 1);
		String getterMessage = msg;
		char[] charArray = getterMessage.toCharArray();
		double totalCount = 0;
		for (char c : charArray) {
			char toDo = c;
			if (Character.isDigit(toDo))
				toDo = (char) c;
			totalCount += Formatter.CharToInt(toDo);
		}
		while (totalCount > 100) {
			totalCount /= 2;
		}
		totalCount /= 10;
		if (totalCount < 1)
			totalCount = 1;
		if (totalCount > 10)
			totalCount = 10;
		Member u = null;
		List<Member> gmems = message.getMentionedMembers();
		if (!gmems.isEmpty())
			u = gmems.get(0);
		if (u == null) {
			List<Member> mems = guild.getMembersByName(msg, true);
			if (!mems.isEmpty())
				u = mems.get(0);
		}
		if (u == null) {
			List<Member> mems = guild.getMembersByEffectiveName(msg, true);
			if (!mems.isEmpty())
				u = mems.get(0);
		}
		if (u != null) {
			if (u.getUser().getMutualGuilds().size() > 3)
				totalCount += 2;
			if (u.hasPermission(Permission.MANAGE_SERVER))
				totalCount += 1;
			if (u.getEffectiveName().length() >= 22)
				totalCount -= 1;
			if (u.isOwner())
				totalCount += 3;
			if (u.getUser().isBot())
				totalCount -= 1;
			if (u.getUser().getId().startsWith("4"))
				totalCount -= 1;
			if (u.getUser().getId().startsWith("2"))
				totalCount += 1;
			if (u.getUser().getIdLong() == u.getJDA().getSelfUser().getIdLong())
				totalCount = 10;
			if (u.getUser().getCreationTime().getYear() > new Date().getYear() - 1)
				totalCount += 1;
		}
		if (totalCount < 1)
			totalCount = 1;
		if (totalCount > 10)
			totalCount = 10;
		if (msg.equalsIgnoreCase("your brother a mother"))
			totalCount = 1000000000;
		if (msg.equalsIgnoreCase("your granny a tranny"))
			totalCount = 11;
		if (msg.equalsIgnoreCase("your sister a mister"))
			totalCount = 9001;
		channel.sendMessage(":thinking: | I'd give " + msg + " a " + (int) totalCount + "/10").queue();
	}
}
