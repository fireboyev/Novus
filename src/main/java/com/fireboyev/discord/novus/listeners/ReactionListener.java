
package com.fireboyev.discord.novus.listeners;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionOptions;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionProfile;
import com.fireboyev.discord.novus.music.Song;
import com.fireboyev.discord.novus.objects.UserFolder;
import com.fireboyev.discord.novus.util.Bot;
import com.fireboyev.discord.novus.util.Questions20Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (Bot.IsFire(event.getMember())) {
            if (event.getMessageId().equalsIgnoreCase(Questions20Util.currentMessage)) {
                //System.out.println(event.getReactionEmote().getName());
                if (event.getReactionEmote().getName().equalsIgnoreCase("\uD83D\uDC4D")) {
                    event.getChannel().retrieveMessageById(event.getMessageId()).complete().delete().reason("because");
                    for (QuestionProfile qp : FileManager.openBotFolder().options.questionProfiles)
                        if (qp.id == Questions20Util.profile) {
                            boolean worked = qp.attributes.add(Questions20Util.question);
                            String msg = "";
                            if (worked)
                                msg = "Successfully added '" + Questions20Util.question + "' to profile '" + qp.name + ":" + qp.id + "'\n";
                            else
                                msg = "Failed to add '" + Questions20Util.question + "' to profile '" + qp.name + ":" + qp.id + "'\n";
                            qp.updates.remove(Questions20Util.question);
                            Questions20Util.profile = 99999;
                            Questions20Util.question = "";
                            Questions20Util.currentMessage = "";
                            QuestionProfile found = null;
                            for (QuestionProfile q : FileManager.openBotFolder().options.questionProfiles) {
                                if (q.updates.size() != 0) {
                                    Questions20Util.question = q.updates.get(0);
                                    Questions20Util.profile = q.id;
                                    found = q;
                                    break;
                                }
                            }
                            if (Questions20Util.question.length() > 1) {
                                for (QuestionOptions qo : FileManager.openBotFolder().options.questionOptions)
                                    if (qo.name.equalsIgnoreCase(Questions20Util.question)) {
                                        msg += "Profile: " + found.name + ":" + found.id + "\n";
                                        msg += qo.question;
                                    }
                            }
                            FileManager.openBotFolder().save();
                            String msgID = event.getChannel().sendMessage(msg).complete().getId();

                            if (Questions20Util.question.length() > 1) {
                                Questions20Util.currentMessage = msgID;
                                event.getChannel().retrieveMessageById(msgID).complete().addReaction("\uD83D\uDC4D").queue();
                                event.getChannel().retrieveMessageById(msgID).complete().addReaction("\uD83D\uDC4E").queue();
                            } else event.getChannel().sendMessage("No more updates found").queue();
                        }
                } else if (event.getReactionEmote().getName().equalsIgnoreCase("\uD83D\uDC4E")) {
                    event.getChannel().retrieveMessageById(event.getMessageId()).complete().delete();
                    for (QuestionProfile qp : FileManager.openBotFolder().options.questionProfiles)
                        if (qp.id == Questions20Util.profile) {
                            qp.updates.remove(Questions20Util.question);
                            Questions20Util.profile = 99999;
                            Questions20Util.question = "";
                            Questions20Util.currentMessage = "";
                            QuestionProfile found = null;
                            for (QuestionProfile q : FileManager.openBotFolder().options.questionProfiles) {
                                if (q.updates.size() != 0) {
                                    Questions20Util.question = q.updates.get(0);
                                    Questions20Util.profile = q.id;
                                    found = q;
                                    break;
                                }
                            }
                            String msg = "QA MANAGER:\n";
                            if (Questions20Util.question.length() > 1) {
                                for (QuestionOptions qo : FileManager.openBotFolder().options.questionOptions)
                                    if (qo.name.equalsIgnoreCase(Questions20Util.question)) {
                                        msg += "Profile: " + found.name + ":" + found.id + "\n";
                                        msg += qo.question;
                                    }
                            }
                            FileManager.openBotFolder().save();
                            String msgID = event.getChannel().sendMessage(msg).complete().getId();

                            if (Questions20Util.question.length() > 1) {
                                Questions20Util.currentMessage = msgID;
                                //event.getChannel().getMessageById(msgID).complete().addReaction("\uD83D\uDC4D").queue();
                                //event.getChannel().getMessageById(msgID).complete().addReaction("\uD83D\uDC4E").queue();
                            } else event.getChannel().sendMessage("No more updates found").queue();
                        }
                } else if (event.getReactionEmote().getName().equals("?")) {

                }
            }
        }
        if (!event.getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
            if (event.getReactionEmote().getName().equals("?")) {
                Guild guild = event.getGuild();
                Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
                if (song != null) {
                    User user = event.getUser();
                    UserFolder folder = FileManager.openUserFolder(user);
                    folder.addSong(song);
                    user.openPrivateChannel().complete().sendMessage("Added to Song Favs: " + song.getName()).queue();
                }
            } else if (event.getReactionEmote().getName().equals("?")) {
                Guild guild = event.getGuild();
                Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
                if (song != null) {
                    if (!Main.getMusicManager().getGuildAudioPlayer(guild).player.getPlayingTrack().getIdentifier()
                            .equalsIgnoreCase(song.getId()))
                        event.getChannel().retrieveMessageById(event.getMessageIdLong()).complete().getReactions().get(1).removeReaction().queue();
                    return;
                }
                int count = 0;
                MessageReaction r = event.getReaction();
                for (User u : r.retrieveUsers().complete()) {
                    if (event.getGuild().getAudioManager().isConnected()) {
                        if (event.getGuild().getAudioManager().getConnectedChannel().getMembers()
                                .contains(event.getGuild().getMember(u))) {
                            count++;
                        }
                    }

                }
                if (event.getGuild().getAudioManager().isConnected()) {
                    int memberCount = event.getGuild().getAudioManager().getConnectedChannel().getMembers().size();
                    memberCount -= 1;
                    count -= 1;
                    if (count >= Math.round(memberCount / 2)) {
                        event.getChannel().sendMessage("Majourity of Users want to skip song ("
                                + Math.round(memberCount / 2) + "/" + memberCount + ")").queue();
                        if (r != null) {
                            r.getTextChannel().retrieveMessageById(r.getMessageId()).complete().getReactions().get(1)
                                    .removeReaction().queue();
                        }
                        Main.getMusicManager().skipTrack(event.getChannel());
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        if (!event.getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
            if (event.getReactionEmote().getName().equals("???")) {
                Guild guild = event.getGuild();
                Song song = Main.getMusicManager().getGuildAudioPlayer(guild).getSong(event.getMessageIdLong());
                if (song != null) {
                    User user = event.getUser();
                    UserFolder folder = FileManager.openUserFolder(user);
                    folder.removeSong(song);
                    user.openPrivateChannel().complete().sendMessage("Removed from Song Favs: " + song.getName())
                            .queue();
                }
            }
        }
    }
}
