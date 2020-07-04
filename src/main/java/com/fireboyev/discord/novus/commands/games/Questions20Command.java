package com.fireboyev.discord.novus.commands.games;

import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionOptions;
import com.fireboyev.discord.novus.util.Bot;
import com.fireboyev.discord.novus.util.Questions20Util;
import com.fireboyev.discord.novus.util.QuestionsGameData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Questions20Command implements CommandExecutor {
    @Override
    public void onCommand(User user, Message message, String[] args, MessageChannel channel,
                          MessageReceivedEvent event) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("start")) {
                Questions20Util.runningGames.put(user.getId(), new QuestionsGameData(user.getId()));
                QuestionOptions question = Questions20Util.runningGames.get(user.getId()).GetQuestion();
                if (question != null) {
                    EmbedBuilder b = new EmbedBuilder();
                    b.setAuthor(user.getName(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
                    b.addField("", question.question, true);
                    b.setTitle("20 Questions!");
                    channel.sendMessage(b.build()).queue();
                }
            } else if (args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("y")) {
                QuestionsGameData ogd = Questions20Util.runningGames.get(user.getId());
                if (ogd != null) {
                    ogd.CorrectQuestion();
                    QuestionOptions question = Questions20Util.runningGames.get(user.getId()).GetQuestion();
                    if (question != null) {
                        EmbedBuilder b = new EmbedBuilder();
                        b.setAuthor(user.getName(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
                        b.addField("", question.question, true);
                        b.setTitle("20 Questions!");
                        channel.sendMessage(b.build()).queue();
                    }
                } else
                    channel.sendMessage("No Questions Game Currently Started!\nUse **" + Bot.getPrefix(channel) + "qc start** to start a game.").queue();
            } else if (args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("n")) {
                QuestionsGameData ogd = Questions20Util.runningGames.get(user.getId());
                if (ogd != null) {
                    QuestionOptions question = Questions20Util.runningGames.get(user.getId()).GetQuestion();
                    if (question != null) {
                        EmbedBuilder b = new EmbedBuilder();
                        b.setAuthor(user.getName(), user.getEffectiveAvatarUrl(), user.getEffectiveAvatarUrl());
                        b.addField("", question.question, true);
                        b.setTitle("20 Questions!");
                        channel.sendMessage(b.build()).queue();
                    }
                } else
                    channel.sendMessage("No Questions Game Currently Started!\nUse **" + Bot.getPrefix(channel) + "qc start** to start a game.").queue();
            }
        } else channel.sendMessage("Not enough arguments! <start|yes|no>").queue();
    }
}
