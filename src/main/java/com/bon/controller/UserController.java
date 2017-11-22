package com.bon.controller;

import com.bon.config.Action;
import com.bon.model.Question;
import com.bon.model.User;
import com.bon.service.QuestionService;
import com.bon.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        if (service == null) {
            throw new IllegalArgumentException();
        }
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new user", notes = "Creates a new user in mongodb database.")
    public ResponseEntity<Void> create(@Validated(Action.Create.class) @RequestBody User user) throws URISyntaxException {
        service.register(user);
        return ResponseEntity.created(new URI("/user/")).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves user data", notes = "Retrieves user data from mongodb database.")
    public User find() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return null;
    }

}