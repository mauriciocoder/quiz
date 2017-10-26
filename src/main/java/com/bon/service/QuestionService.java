package com.bon.service;

import com.bon.model.Question;
import com.bon.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Component
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QuestionRepository questionRepo;

    public Question create(Question question) {
        log.info("creating question: " + question);
        Question duplicated = questionRepo.findByQuestioning(question.getQuestioning());
        if (duplicated != null) {
            log.info("question: " + question + " already exists!");
            throw new ResourceAccessException("Question already exists");
        }
        return questionRepo.save(question);
    }

    public Question update(Question question) {
        String id = question.getId();
        log.info("updating question with id: " + id);
        if (!questionRepo.exists(id)) {
            log.info("question with id: " + id + " does not exists!");
            throw new ResourceNotFoundException(null, null);
        }
        return questionRepo.save(question);
    }

    public List<Question> findAll() {
        log.info("finding all questions");
        List<Question> all = questionRepo.findAll();
        if (CollectionUtils.isEmpty(all)) {
            log.info("database is empty");
            throw new ResourceNotFoundException(null, null);
        }
        return all;
    }

    public Question find(String id) {
        log.info("finding question with id: " + id);
        if (!questionRepo.exists(id)) {
            log.info("question with id: " + id + " does not exists!");
            throw new ResourceNotFoundException(null, null);
        }
        return questionRepo.findOne(id);
    }

    public void delete(String id) throws ResourceNotFoundException {
        log.info("deleting question with id: " + id);
        if (!questionRepo.exists(id)) {
            log.info("question with id: " + id + " does not exists!");
            throw new ResourceNotFoundException(null, null);
        }
        questionRepo.delete(id);
    }
}
