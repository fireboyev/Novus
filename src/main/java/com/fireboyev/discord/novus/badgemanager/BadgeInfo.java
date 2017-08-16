package com.fireboyev.discord.novus.badgemanager;

import java.awt.image.BufferedImage;

public class BadgeInfo {
	public BufferedImage image;
	public String name;
	public int cost;

	public BadgeInfo(String name, BufferedImage image, int cost) {
		this.name = name;
		this.image = image;
		this.cost = cost;
	}
}
