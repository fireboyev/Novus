package com.fireboyev.discord.novus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import com.fireboyev.discord.novus.objects.UserFolder;

import net.dv8tion.jda.core.entities.User;

public class ImageBuilder {
	public static BufferedImage buildImage(User user) {
		BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
		UserFolder folder = FileManager.openUserFolder(user);
		Graphics2D g2 = image.createGraphics();
		g2.fillRect(0, 0, 500, 500);
		g2.drawImage(getProfile(user), 0, 200, 128, 128, getImageObserver());
		g2.setColor(Color.GREEN);
		g2.setBackground(Color.WHITE);
		g2.setFont(new Font(Font.SERIF, 40, 30));
		g2.drawString("Songs: " + folder.getSongs().size(), 10, 400);
		g2.drawString("Novus Enhanced Guilds: " + user.getMutualGuilds().size(), 10, 460);
		g2.dispose();
		return image;
	}

	private static BufferedImage getProfile(User user) {
		BufferedImage profile = null;
		try {
			URL url = new URL(user.getAvatarUrl());
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			profile = ImageIO.read(con.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return profile;
	}

	private static ImageObserver getImageObserver() {
		return new ImageObserver() {

			@Override
			public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4,
					int paramInt5) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}
}
