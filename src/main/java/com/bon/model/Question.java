package com.bon.model;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.List;

public class Question {

    @Id
    private String id;
    @NotNull(message = "Answers must not be null")
    private List<String> answers;
    private int correctAnswerIndex;

    @AssertTrue(message = "correctAnswerIndex is not in the range of answers")
    private boolean isCorrectAnswerIndexValid() {
        int correctAnswerIndex = getCorrectAnswerIndex();
        int answersSize = getAnswers().size();
        return correctAnswerIndex >= 0 && correctAnswerIndex < answersSize;
    }

    public Question() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", answers=" + answers +
                ", correctAnswerIndex=" + correctAnswerIndex +
                '}';
    }
}