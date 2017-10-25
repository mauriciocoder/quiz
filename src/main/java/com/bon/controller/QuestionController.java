package com.bon.controller;

import com.bon.model.Question;
import com.bon.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping(method=RequestMethod.POST)
    @ApiOperation(value = "Creates a new question", notes = "Creates a new question in mongodb database.", response = Question.class)
    @ResponseBody
    public Question create(@Valid @RequestBody Question question) {
        return questionService.create(question);
    }

    @RequestMapping(method=RequestMethod.GET)
    @ApiOperation(value = "Retrieves every question", notes = "Retrieves every question from mongodb database.", response = List.class)
    @ResponseBody
    public ResponseEntity<?> findAll() {
        List<Question> questions = questionService.findAll();
        if (!CollectionUtils.isEmpty(questions)) {
            return new ResponseEntity<List>(questions, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method=RequestMethod.GET)
    @ApiOperation(value = "Retrieves the question with the provided id", notes = "Retrieves the question with the provided id from mongodb database.")
    public ResponseEntity<?> find(@PathVariable(value = "id", required = true) String id) {
        Question question = questionService.find(id);
        if (question != null) {
            return new ResponseEntity<Question>(question, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}