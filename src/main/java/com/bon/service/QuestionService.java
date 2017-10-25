package com.bon.service;

import com.bon.model.Question;
import com.bon.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Component
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QuestionRepository questionRepo;

    public Question create(Question q) {
        log.info("creating question: " + q);
        Question duplicated = questionRepo.findByQuestioning(q.getQuestioning());
        if (duplicated != null) {
            log.info("question: " + q + " already exists!");
            throw new ResourceAccessException("Question already exists");
        }
        return questionRepo.save(q);
    }

    public Question update(Question q) {
        log.info("updating question: " + q);
        return questionRepo.save(q);
    }

    public List<Question> findAll() {
        log.info("finding all questions");
        List<Question> all = questionRepo.findAll();
        if (CollectionUtils.isEmpty(all)) {
            log.info("database is empty");
            throw new ResourceNotFoundException("all", null);  // FIXME: Fix null parameter
        }
        return all;
    }

    public Question find(String id) {
        log.info("finding question with id: " + id);
        if (!questionRepo.exists(id)) {
            log.info("question with id: " + id + " does not exists!");
            throw new ResourceNotFoundException(id, null);  // FIXME: Fix null parameter
        }
        return questionRepo.findOne(id);
    }

    public void delete(String id) throws ResourceNotFoundException {
        log.info("deleting question with id: " + id);
        if (!questionRepo.exists(id)) {
            log.info("question with id: " + id + " does not exists!");
            throw new ResourceNotFoundException(id, null);
        }
        questionRepo.delete(id);
    }
}
