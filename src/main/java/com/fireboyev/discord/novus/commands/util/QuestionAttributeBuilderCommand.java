package com.fireboyev.discord.novus.commands.util;

import com.fireboyev.discord.novus.Main;
import com.fireboyev.discord.novus.commandmanager.CommandExecutor;
import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionOptions;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionProfile;
import com.fireboyev.discord.novus.util.Bot;
import com.fireboyev.discord.novus.util.Questions20Util;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;

public class QuestionAttributeBuilderCommand implements CommandExecutor {
    @Override
    public void onCommand(User user, Message message, String[] args, MessageChannel channel, MessageReceivedEvent event) {
        if (Bot.IsFire(user)) {
            if (args.length > 2) {
                if (args[1].equalsIgnoreCase("addprofile")) {
                    String name = "";
                    for (int i = 0; i < args.length - 3; i++) {
                        name += args[2 + i];
                    }
                    try {
                        int id = Integer.parseInt(args[args.length - 1]);
                        boolean worked = Main.q20util.registerProfile(name, id);
                        FileManager.openBotFolder().save();
                        if (worked)
                            channel.sendMessage("Successfully added '" + name + "' to the profile list with id '" + id + "'").queue();
                        else channel.sendMessage("Something went wrong!").queue();
                    } catch (Exception e) {
                        channel.sendMessage("ID must be a number!").queue();
                    }
                } else if (args[1].equalsIgnoreCase("addquestion")) {
                  String entire = "";
                    for (int i = 0; i < args.length-3; i++) {
                        entire += args[3+i] + " ";
                    }

                    boolean worked = Main.q20util.registerQuestion(args[2], entire);
                    FileManager.openBotFolder().save();
                    if (worked)
                        channel.sendMessage("Successfully added '" + entire + "' to the list with tag: '" + args[2] + "'").queue();
                    else channel.sendMessage("Something went wrong!").queue();

                } else if (args[1].equalsIgnoreCase("updates")) {
                    Questions20Util.currentMessage = channel.sendMessage("reaction test").complete().getId();
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
                    String msgID = event.getChannel().sendMessage(msg).complete().getId();

                    if (Questions20Util.question.length() > 1) {
                        Questions20Util.currentMessage = msgID;
                        event.getChannel().retrieveMessageById(msgID).complete().addReaction("\uD83D\uDC4D").queue();
                        event.getChannel().retrieveMessageById(msgID).complete().addReaction("\uD83D\uDC4E").queue();
                    } else event.getChannel().sendMessage("No more updates found").queue();
                }
            }
        }
    }
}
