package com.bon.repository;

import com.bon.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
    public Question findByQuestioning(String questioning);
}