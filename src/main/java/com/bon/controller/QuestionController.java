package com.bon.controller;

import com.bon.model.Question;
import com.bon.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping(method=RequestMethod.POST)
    @ApiOperation(value = "Creates a new question", notes = "Creates a new question in mongodb database.")
    @ResponseBody
    public ResponseEntity<Void> create(@Validated(Question.Create.class) @RequestBody Question question) throws URISyntaxException {
        try {
            Question q = questionService.create(question);
            return ResponseEntity.created(new URI("/question/" + q.getId())).build();
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @RequestMapping(method=RequestMethod.GET)
    @ApiOperation(value = "Retrieves every question", notes = "Retrieves every question from mongodb database.")
    @ResponseBody
    public ResponseEntity<List<Question>> findAll() {
        try {
            List<Question> questions = questionService.findAll();
            return ResponseEntity.ok(questions);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    @ApiOperation(value = "Retrieves the question with the provided id", notes = "Retrieves the question with the provided id from mongodb database.")
    public ResponseEntity<Question> find(@PathVariable(value = "id", required = true) String id) {
        try {
            Question question = questionService.find(id);
            return ResponseEntity.ok(question);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
    @ApiOperation(value = "Deletes a question by id", notes = "Deletes a question by id in mongodb database.")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) String id) {
        try {
            questionService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}