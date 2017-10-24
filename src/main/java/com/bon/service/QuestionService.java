package com.bon.service;

import com.bon.model.Question;
import com.bon.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QuestionRepository questionRepo;

    public Question create(Question q) {
        log.info("creating question: " + q);
        return questionRepo.save(q);
    }

    public Question update(Question q) {
        log.info("updating question: " + q);
        return questionRepo.save(q);
    }

    public List<Question> findAll() {
        log.info("finding all questions");
        return questionRepo.findAll();
    }
}
