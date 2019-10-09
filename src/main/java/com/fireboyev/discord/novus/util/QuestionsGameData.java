package com.fireboyev.discord.novus.util;

import com.fireboyev.discord.novus.filestorage.FileManager;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionOptions;
import com.fireboyev.discord.novus.filestorage.config.bot.QuestionProfile;

import java.util.ArrayList;
import java.util.List;

public class QuestionsGameData {
    List<String> askedQuestions;
    List<String> correctQuestions;
    String userId;
    String currentQuestion;

    public QuestionsGameData(String userId) {
        askedQuestions = new ArrayList<>();
        correctQuestions = new ArrayList<>();
        this.userId = userId;
        currentQuestion = "";
    }

    public QuestionOptions GetQuestion() {
        List<QuestionOptions> questions = FileManager.openBotFolder().options.questionOptions;
        for (QuestionOptions qo : questions) {
            for (String q : correctQuestions)
                if (qo.depends.size() == 0 || qo.depends.contains(q)) {
                    if (!askedQuestions.contains(qo.name)) {
                        askedQuestions.add(qo.name);
                        currentQuestion = qo.name;
                        return qo;
                    }
                }
        }
        return null;
    }
    public void CorrectQuestion(){
        correctQuestions.add(currentQuestion);
    }
    public List<QuestionProfile> PossibleProfiles(){
        return new ArrayList<>();
    }
}
