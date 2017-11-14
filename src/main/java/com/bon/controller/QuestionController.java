package com.bon.controller;

import com.bon.config.Action;
import com.bon.model.Question;
import com.bon.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService service;

    @Autowired
    public QuestionController(QuestionService service) {
        if (service == null) {
            throw new IllegalArgumentException();
        }
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new question", notes = "Creates a new question in mongodb database.")
    public ResponseEntity<Void> create(@Validated(Action.Create.class) @RequestBody Question question) throws URISyntaxException {
        Question q = service.create(question);
        return ResponseEntity.created(new URI("/question/" + q.getId())).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves every question", notes = "Retrieves every question from mongodb database.")
    public List<Question> findAll() {
        return service.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves the question with the provided id", notes = "Retrieves the question with the provided id from mongodb database.")
    public Question find(@PathVariable(value = "id", required = true) String id) {
        return service.find(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Updates the question with the provided id", notes = "Updates the question with the provided id from mongodb database. It does not create if non exist")
    public ResponseEntity update(@PathVariable(value = "id", required = true) String id, @Validated(Action.Update.class) @RequestBody Question question) {
        if (!id.equalsIgnoreCase(question.getId())) {
            return ResponseEntity.badRequest().body("Cannot update question with an id different from path variable id");
        }
        service.update(question);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "Deletes a question by id", notes = "Deletes a question by id in mongodb database.")
    public ResponseEntity<Void> delete(@PathVariable(value = "id", required = true) String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}