package com.bon.service;

import com.bon.model.Question;
import com.bon.model.User;
import com.bon.repository.QuestionRepository;
import com.bon.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Component
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException();
        }
        this.repository = repository;
    }

    public User register(User user) {
        log.info("registering user: " + user);
         User duplicated = repository.findByUsername(user.getUsername());
        if (duplicated != null) {
            log.info("username: " + user.getUsername() + " already exists!");
            throw new ResourceAccessException("User already exists");
        }
        return repository.save(user);
    }
}
