package com.fireboyev.discord.novus.filestorage.config.user;

import java.util.ArrayList;
import java.util.List;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BadgeOptions {
	@Setting("owned")
	private List<String> owned = new ArrayList<>();
	@Setting("active")
	private ActiveBadgeOptions active = new ActiveBadgeOptions();

	public List<String> getOwned() {
		return owned;
	}

	public void addOwned(String badge) {
		this.owned.add(badge);
	}

	public ActiveBadgeOptions getActive() {
		return active;
	}

}
