package com.fireboyev.discord.novus.censormanager.config;

import java.util.List;
import java.util.StringTokenizer;

import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.objects.GuildFolder;

import net.dv8tion.jda.core.entities.Guild;

public class CensorUtil {
	public boolean containsCensor(String msg, Guild guild) {
		GuildFolder folder = FileManager.openGuildFolder(guild);
		List<String> censoredWords = folder.options.censoring.censoredWords;
		if (censoredWords.isEmpty())
			return false;
		msg = msg.toLowerCase();
		msg = " " + msg + " ";
		msg = msg.replace(".", "");
		msg = msg.replace(",", "");
		msg = msg.replace("!", "");
		msg = msg.replace("?", "");
		msg = msg.replace("-", "");
		msg = msg.replace("\"", "");
		msg = msg.replace("'", "");
		String msgWithSpace = msg;
		msg = msg.replace(" ", "");
		String msgWithoutSpace = msg;
		// StringTokenizer token = new StringTokenizer(msg); I'll see about this
		for (String str : censoredWords) {
			if (str.contains("%s"))
				msg = msgWithSpace;
			String newStr = str.replace("%s", " ");
			if (msg.contains(newStr))
				return true;
			msg = msgWithoutSpace;
		}
		return false;
	}
}
