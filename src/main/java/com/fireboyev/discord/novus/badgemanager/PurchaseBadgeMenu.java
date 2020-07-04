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
 */package com.fireboyev.discord.novus.badgemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.filestorage.FileManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class PurchaseBadgeMenu {
	TextChannel tc;
	User user;
	int page;
	List<HashMap<Integer, BadgeInfo>> pages;
	HashMap<Integer, BadgeInfo> currentPage;
	boolean isPage;
	BadgeInfo selected = null;
	Message lastMessage;
	Iterator<HashMap<Integer, BadgeInfo>> pageIter;

	public PurchaseBadgeMenu(User user, TextChannel tc) {
		this.tc = tc;
		this.user = user;
		pages = new ArrayList<>();
		page = 0;
		List<String> owned = FileManager.openUserFolder(user).options.getBadges().getOwned();
		List<BadgeInfo> purchasable = new ArrayList<>();
		for (BadgeInfo info : FileManager.getAllBadges()) {
			if (!owned.contains(info.name)) {
				purchasable.add(info);
			}
		}
		int totalCount = 0;
		int count = 1;
		HashMap<Integer, BadgeInfo> current = new HashMap<Integer, BadgeInfo>();
		for (BadgeInfo info : purchasable) {
			current.put(count, info);
			count++;
			if (count >= 8 || totalCount >= purchasable.size() - 1) {
				pages.add(current);
				current = new HashMap<Integer, BadgeInfo>();
				count = 1;
			}
			totalCount++;
		}
		pageIter = pages.iterator();
	}

	public void open() {
		EmbedBuilder builder = new EmbedBuilder();
		if (!pageIter.hasNext()) {
			tc.sendMessage("It seems that there was an error registering the badges, please try again later.").queue();
			close();
			return;
		}
		HashMap<Integer, BadgeInfo> pageList = pageIter.next();
		currentPage = pageList;
		for (int i : pageList.keySet()) {
			BadgeInfo info = pageList.get(i);
			builder.addField("", "**[" + i + "]" + "     Name:** " + info.name + " | **Cost:** " + info.cost, false);
		}
		builder.addField("", "**[8]" + "     Next Page**", false);
		builder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
		builder.setDescription("Type the number of the selection you like");
		builder.setFooter("Type `cancel` to cancel badge purchase", null);
		isPage = true;
		lastMessage = tc.sendMessage(builder.build()).complete();
	}

	public void trigger(Message msg) {
		if (msg.getContentRaw().equalsIgnoreCase("cancel")) {
			close();
			return;
		}
		if (isPage) {
			try {
				for (int i : currentPage.keySet()) {
					if (Integer.parseInt(msg.getContentDisplay()) == i) {
						BadgeInfo info = currentPage.get(i);
						if (info != null) {
							selected = info;
							EmbedBuilder builder = new EmbedBuilder();
							builder.addField("Are You Sure?", "", false);
							builder.addField("Name: " + info.name, "Cost: " + info.cost, false);
							builder.addField("[1] YES", null, false);
							builder.addField("[2] NO", null, false);
							//builder.setImage();
						} else {
							tc.sendMessage("An Internal Error was Encountered!").queue();
							close();
						}
						return;
					}
				}
			} catch (Exception e) {
				tc.sendMessage("Invalid Selection.").queue(m -> {
					try {
						Thread.sleep(10000);
						m.delete().queue();
					} catch (Exception e1) {
						m.delete().queue();
					}
				});
			}
		}
	}

	private void printPage() {
		if (lastMessage != null) {
			if (tc.retrieveMessageById(lastMessage.getId()).complete() != null) {
				lastMessage.delete().queue();
			}
		}
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
		builder.setDescription("Type the number of the selection you like");
		builder.setFooter("Type `cancel` to cancel badge purchase", null);

		int count = 1;
		for (int i : currentPage.keySet()) {
			count++;
			BadgeInfo info = currentPage.get(i);
			builder.addField("", "**[" + i + "]" + "     Name:** " + info.name + " | **Cost:** " + info.cost, false);
		}
		if (pageIter.hasNext()) {
			builder.addField("", "**[" + count + 1 + "]" + "     Next Page**", false);
			count++;
		}
		if (!currentPage.equals(pages.get(0))) {
			builder.addField("", "**[" + count + 1 + "]" + "     Last Page**", false);
		}
		lastMessage = tc.sendMessage(builder.build()).complete();
	}

	public void close() {
		Main.getBadgeManager().badgeMenus.remove(user.getIdLong());
		if (lastMessage != null) {
			if (tc.retrieveMessageById(lastMessage.getId()).complete() != null) {
				lastMessage.delete().queue();
			}
		}
	}
}
