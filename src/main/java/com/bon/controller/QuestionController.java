package com.bon.controller;

import com.bon.model.Question;
import com.bon.repository.QuestionRepository;
import com.bon.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping(method=RequestMethod.POST)
    public @ResponseBody Question create(@Valid @RequestBody Question question) {
        return questionService.create(question);
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody List<Question> findAll() {
        return questionService.findAll();
    }

}