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
 */
package com.fireboyev.discord.novus.badgemanager;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BadgeManager {
	HashMap<Long, PurchaseBadgeMenu> badgeMenus;

	public BadgeManager() {
		badgeMenus = new HashMap<Long, PurchaseBadgeMenu>();
	}

	public PurchaseBadgeMenu registerMenu(User user, TextChannel tc) {
		PurchaseBadgeMenu bm = new PurchaseBadgeMenu(user, tc);
		badgeMenus.put(user.getIdLong(), bm);
		bm.open();
		return bm;
	}

	public void triggerMenu(User user, Message msg) {
		if (!badgeMenus.containsKey(user.getIdLong()))
			return;
		PurchaseBadgeMenu bm = badgeMenus.get(user.getIdLong());
		bm.trigger(msg);

	}
}
