package com.fireboyev.discord.novus.util;

import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionOptions;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionProfile;

import java.util.HashMap;
import java.util.List;

public class Questions20Util {
    public static HashMap<String, QuestionsGameData> runningGames = new HashMap<>();
    public static String currentMessage = "";
    public static String question = "";
    public static int profile = 99999;
    public Questions20Util() {

    }

    public void LoadProfiles() {
        System.out.println("Loading Question Profiles...");

    }

    public boolean registerQuestion(String name, String question) {
        return FileManager.openBotFolder().options.questionOptions.add(new QuestionOptions(name, question));
    }

    public boolean addDependancies(String questionName, List<String> dependancies) {
        for (QuestionOptions qo : FileManager.openBotFolder().options.questionOptions) {
            if (qo.name.equalsIgnoreCase(questionName))
                return qo.depends.addAll(dependancies);
        }
        return false;
    }

    public boolean registerProfile(String name, int id) {
        return FileManager.openBotFolder().options.questionProfiles.add(new QuestionProfile(name, id));
    }

    public boolean updateProfile(int id, String attribute) {
        for (QuestionProfile p : FileManager.openBotFolder().options.questionProfiles) {
            if (p.equals(id)) {
                p.attributes.add(attribute);
                return true;
            }
        }
        return false;
    }
}
