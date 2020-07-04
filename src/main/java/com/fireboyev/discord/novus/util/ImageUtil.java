package com.fireboyev.discord.novus.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageHistory;

public class ImageUtil {
	public static URL getLastImage(MessageHistory history) {
		for (Message msg : history.retrievePast(25).complete()) {
			try {
				for (Attachment at : msg.getAttachments()) {
					if (at.isImage()) {
						return new URL(at.getProxyUrl());
					}
				}
				List<String> strs = extractUrls(msg.getContentRaw());
				if (strs.isEmpty())
					continue;
				URL url = new URL(strs.get(0));
				//if (url.toString().startsWith("https://cdn.discordapp.com/attachments/"))
				return url;
			} catch (MalformedURLException e) {
				continue;

			}
		}
		return null;
	}

	public static List<String> extractUrls(String text) {
		List<String> containedUrls = new ArrayList<String>();
		String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(text);

		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
		}

		return containedUrls;
	}
}
