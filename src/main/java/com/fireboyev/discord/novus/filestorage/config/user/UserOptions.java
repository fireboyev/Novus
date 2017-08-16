package com.fireboyev.discord.novus.filestorage.config.user;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class UserOptions {
	@Setting("tokens")
	private int tokens = 200;
	@Setting("badges")
	private BadgeOptions badges = new BadgeOptions();

	public int getTokens() {
		return tokens;
	}

	public BadgeOptions getBadges() {
		return badges;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

}
