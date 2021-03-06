package com.fireboyev.discord.novus.commands.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.util.ImageUtil;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InvertCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		URL url = null;
		if (args.length == 2) {
			try {
				URI uri = URI.create(args[1]);
				url = uri.toURL();
				if (!url.toString().startsWith("https://cdn.discordapp.com/attachments/"))
				{
//					channel.sendMessage("Only Discord URLs allowed.").queue();
//					return;
				}
			} catch (Exception e) {
				channel.sendMessage("Invalid URL!").queue();
				return;
			}
		} else {
			url = ImageUtil.getLastImage(channel.getHistory());
		}
		if (url == null) {
			channel.sendMessage("No Image Found.").queue();
			return;
		}
		BufferedImage inputFile = null;
		try {
			channel.sendTyping().queue();
			final HttpURLConnection connection = (HttpURLConnection) url
			        .openConnection();
			connection.setRequestProperty(
			    "User-Agent",
			    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
			inputFile = ImageIO.read(connection.getInputStream());
			for (int x = 0; x < inputFile.getWidth(); x++) {
	            for (int y = 0; y < inputFile.getHeight(); y++) {
	                int rgba = inputFile.getRGB(x, y);
	                Color col = new Color(rgba, true);
	                col = new Color(255 - col.getRed(),
	                                255 - col.getGreen(),
	                                255 - col.getBlue());
	                inputFile.setRGB(x, y, col.getRGB());
	            }
	        }
		
		} catch (IOException e) {
			channel.sendMessage("Unable to retrieve the Image.").queue();
			channel.sendMessage(e.getMessage() + " | " + e.getLocalizedMessage()).queue();
			e.printStackTrace();
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(inputFile, "png", os);
		} catch (IOException e) {
			channel.sendMessage("error writing image").queue();
			e.printStackTrace();
		}
		channel.sendFile(os.toByteArray(), "image.png").queue();

	}

}
