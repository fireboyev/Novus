package com.fireboyev.discord.novus.badgemanager;

import java.util.HashMap;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class BadgeManager {
	HashMap<Long, PurchaseBadgeMenu> badgeMenus;

	public BadgeManager() {
		badgeMenus = new HashMap<Long, PurchaseBadgeMenu>();
	}

	public PurchaseBadgeMenu registerMenu(Member member, TextChannel tc) {
		PurchaseBadgeMenu bm = new PurchaseBadgeMenu(member, tc);
		badgeMenus.put(member.getUser().getIdLong(), bm);
		return bm;
	}

	public void triggerMenu(Member member, Message msg) {
		if (!badgeMenus.containsKey(member.getUser().getIdLong()))
			return;
		PurchaseBadgeMenu bm = badgeMenus.get(member.getUser().getIdLong());
		bm.trigger(msg);
		
	}
}
