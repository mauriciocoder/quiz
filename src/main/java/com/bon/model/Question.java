package com.bon.model;

import com.bon.config.Action;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

public class Question {

    @Id
    @NotNull(groups = {Action.Update.class})
    @Null(groups = {Action.Create.class})
    private String id;

    @NotNull(groups = {Action.Update.class, Action.Create.class}, message = "Questioning must not be null")
    private String questioning;

    @NotNull(groups = {Action.Update.class, Action.Create.class}, message = "Answers must not be null")
    private List<String> answers;

    private int correctAnswerIndex;

    public Question() {}

    public Question(String questioning, List<String> answers, int correctAnswerIndex) {
        this.questioning = questioning;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @AssertTrue(groups = {Action.Update.class, Action.Create.class}, message = "correctAnswerIndex is not in the range of answers")
    private boolean isCorrectAnswerIndexValid() {
        int correctAnswerIndex = getCorrectAnswerIndex();
        int answersSize = getAnswers().size();
        return correctAnswerIndex >= 0 && correctAnswerIndex < answersSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestioning() {
        return questioning;
    }

    public void setQuestioning(String questioning) {
        this.questioning = questioning;
    }

    public List<String> getAnswers() {
        return answers != null ? answers : new ArrayList<>();
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
                ", questioning='" + questioning + '\'' +
                ", answers=" + answers +
                ", correctAnswerIndex=" + correctAnswerIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (correctAnswerIndex != question.correctAnswerIndex) return false;
        if (!questioning.equals(question.questioning)) return false;
        return answers.equals(question.answers);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (questioning != null ? questioning.hashCode() : 0);
        result = 31 * result + (answers != null ? answers.hashCode() : 0);
        result = 31 * result + correctAnswerIndex;
        return result;
    }
}