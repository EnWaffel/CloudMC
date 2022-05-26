package net.projectp.cloudmc.command;

import net.projectp.cloudmc.util.result.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionSequence {

    private final SequenceCallback callback;
    private int currentQuestion = 0;
    private final List<String> questions = new ArrayList<>();

    public QuestionSequence(SequenceCallback callback, String... questions) {
        this.callback = callback;
        this.questions.addAll(Arrays.asList(questions));
    }

    public Result call(String text) {
        return callback.call(currentQuestion, text);
    }

    public SequenceCallback getCallback() {
        return callback;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setCurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

}
