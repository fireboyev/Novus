package com.fireboyev.discord.novus.commands.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.util.ImageUtil;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VectorizeCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {
		URL url = null;
		if (args.length == 2) {
			try {
				URI uri = URI.create(args[1]);
				url = uri.toURL();
				if (!url.toString().startsWith("https://cdn.discordapp.com/attachments/")) {
					// channel.sendMessage("Only Discord URLs allowed.").queue();
					// return;
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
			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
			inputFile = ImageIO.read(connection.getInputStream());
			HashMap<String, Float> options = new HashMap<String, Float>();

			// Tracing
			options.put("ltres", 1f);
			options.put("qtres", 1f);
			options.put("pathomit", 8f);

			// Color quantization
			options.put("colorsampling", 1f); // 1f means true ; 0f means false: starting with generated palette
			options.put("numberofcolors", 16f);
			options.put("mincolorratio", 0.02f);
			options.put("colorquantcycles", 3f);

			// SVG rendering
			options.put("scale", 1f);
			options.put("roundcoords", 1f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3
											// places, like 7.356 ; etc.
			options.put("lcpr", 0f);
			options.put("qcpr", 0f);
			options.put("desc", 1f); // 1f means true ; 0f means false: SVG descriptions deactivated
			options.put("viewbox", 0f); // 1f means true ; 0f means false: fixed width and height

			// Selective Gauss Blur
			options.put("blurradius", 0f); // 0f means deactivated; 1f .. 5f : blur with this radius
			options.put("blurdelta", 20f); // smaller than this RGB difference will be blurred

			// Palette
			// This is an example of a grayscale palette
			// please note that signed byte values [ -128 .. 127 ] will be converted to [ 0
			// .. 255 ] in the getsvgstring function
			byte[][] palette = new byte[8][4];
			for (int colorcnt = 0; colorcnt < 8; colorcnt++) {
				palette[colorcnt][0] = (byte) (-128 + colorcnt * 32); // R
				palette[colorcnt][1] = (byte) (-128 + colorcnt * 32); // G
				palette[colorcnt][2] = (byte) (-128 + colorcnt * 32); // B
				palette[colorcnt][3] = (byte) 127; // A
			}
			//inputFile = createImageFromSVG(ImageTracer.imageToSVG(inputFile, options, palette), inputFile.getWidth(),
				//	inputFile.getHeight());
		} catch (Exception e) {
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

	public BufferedImage createImageFromSVG(String svg, int width, int height) {
		ByteArrayInputStream input = new ByteArrayInputStream(svg.getBytes());
		try {
			return ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
