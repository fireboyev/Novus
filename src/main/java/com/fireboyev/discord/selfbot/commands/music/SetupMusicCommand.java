package com.fireboyev.discord.selfbot.commands.music;

import java.util.ArrayList;
import java.util.List;

import com.fireboyev.discord.selfbot.Bot;
import com.fireboyev.discord.selfbot.commandmanager.CommandExecutor;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.requests.restaction.ChannelAction;

public class SetupMusicCommand extends CommandExecutor {
	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			GuildMessageReceivedEvent event) {
		if (Bot.IsAdmin(member)) {
			if (guild.getTextChannelsByName("music", true).size() == 0) {
				guild.getController().createTextChannel("music").setTopic("The Music Channel").complete();
			}

			if (guild.getVoiceChannelsByName("Music", true).size() == 0) {
				ChannelAction ca = guild.getController().createVoiceChannel("Music");
				List<Permission> allow = new ArrayList<Permission>();
				List<Permission> deny = new ArrayList<Permission>();
				deny.add(Permission.VOICE_SPEAK);
				ca.addPermissionOverride(guild.getPublicRole(), allow, deny);
				ca.complete();
			}
			event.getChannel().sendMessage("Music Setup Complete.").queue();
		} else {
			event.getChannel().sendMessage("You Don't Have Permission, " + event.getAuthor().getAsMention()).queue();
		}
	}
}
