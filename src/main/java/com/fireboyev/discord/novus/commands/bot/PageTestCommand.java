package com.fireboyev.discord.novus.commands.bot;

import java.awt.Color;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.GuildCommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.jagrosh.jdautilities.menu.Paginator;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PageTestCommand implements GuildCommandExecutor {

	@Override
	public void onCommand(Guild guild, User user, Member member, Message message, String[] args, MessageChannel channel,
			MessageReceivedEvent event) {

		Paginator p = new Paginator.Builder()
				.setItems(FileManager.openGuildFolder(guild).getCompliments().toArray(new String[] {}))
				.setItemsPerPage(5).useNumberedItems(true).allowTextInput(true).wrapPageEnds(true)
				.setLeftRightText("> ",  "<").setEventWaiter(Main.waiter).setColor(Color.BLUE).setText("The Test Selection").build();
		p.paginate(channel, 1);
		/*
		 * SelectionDialog.Builder sdb = new SelectionDialog.Builder() .setCanceled(new
		 * Consumer<Message>() {
		 * 
		 * @Override public void accept(Message t) { t.delete().queue(); }
		 * }).setEventWaiter(Main.waiter).setSelectedEnds("-> ", " <-")
		 * .addChoices(FileManager.openGuildFolder(guild).getCompliments().toArray(new
		 * String[] {})) .setColor(Color.ORANGE).useLooping(true).
		 * setText("Select the compliment you would like to delete.");
		 * sdb.setSelectionConsumer(new BiConsumer<Message, Integer>() {
		 * 
		 * @Override public void accept(Message t, Integer u) { channel.sendMessage(
		 * "Compliment: " +
		 * FileManager.openGuildFolder(guild).getCompliments().get(u.intValue()) +
		 * " has been deleted!") .queue();
		 * sdb.setChoices(FileManager.openGuildFolder(guild).getCompliments().toArray(
		 * new String[] {})); sdb.build().showDialog(t, u.intValue());
		 * 
		 * } }); sdb.build().display(channel);
		 */
		/*
		 * ButtonMenu om = new
		 * ButtonMenu.Builder().setChoices(FileManager.openGuildFolder(guild).
		 * getCompliments().toArray(new
		 * String[]{})).setUsers(user).setColor(Color.ORANGE).setEventWaiter(Main.waiter
		 * ).setDescription("Say the number of the compliment you would like to remove")
		 * .setAction(new Consumer<MessageReaction.ReactionEmote>() {
		 * 
		 * @Override public void accept(ReactionEmote t) {
		 * System.out.println(t.getName());
		 * 
		 * } }).build();
		 * 
		 * om.display(channel);
		 */
	}

}
