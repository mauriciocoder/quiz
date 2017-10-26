package com.bon.controller;

import com.bon.ControllerBaseTest;
import com.bon.application.Application;
import com.bon.model.Question;
import com.bon.repository.QuestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class QuestionControllerTest extends ControllerBaseTest {

    @Autowired
    private QuestionRepository questionRepo;

    private Question q1, q2;

    @Before
    public void setup() throws Exception {
        questionRepo.deleteAll();
        q1 = questionRepo.save(new Question("q1", Arrays.asList("Answer1", "Answer2", "Answer3"), 0));
        q2 = questionRepo.save(new Question("q2", Arrays.asList("Answer1", "Answer2", "Answer3"), 0));
    }

    @Test
    public void createValidQuestion() throws Exception {
        Question q = new Question("q", Arrays.asList("Answer1", "Answer2", "Answer3"), 0);
        MvcResult result = doPost(q, status().isCreated());
        // check returned result
        String location = result.getResponse().getHeader("Location").toString();
        result = doGetByLocation(location, status().isOk());
        // check returned result
        String stringResult = result.getResponse().getContentAsString();
        Question qReturned = new ObjectMapper().readValue(stringResult, Question.class);
        Assert.assertNotNull(qReturned.getId());
        Assert.assertEquals(q, qReturned);
    }

    @Test
    public void createInvalidQuestion_QuestionWithId() throws Exception {
        doPost(q1, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_DuplicatedQuestioning() throws Exception {
        Question q = new Question("q1", Arrays.asList("Answer1", "Answer2", "Answer3"), 0);
        doPost(q, status().isConflict());
    }

    @Test
    public void createInvalidQuestion_NullQuestioning() throws Exception {
        Question q = new Question(null, Arrays.asList("Answer1", "Answer2", "Answer3"), 0);
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_EmptyAnswers() throws Exception {
        Question q = new Question("q", new ArrayList<>(), 0);
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_NullAnswers() throws Exception {
        Question q = new Question("q", null, 0);
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_OutOfRangeCorrectAnswerIndex_below() throws Exception {
        Question q = new Question(null, Arrays.asList("Answer1", "Answer2", "Answer3"), -1);
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_OutOfRangeCorrectAnswerIndex_after() throws Exception {
        Question q = new Question(null, Arrays.asList("Answer1", "Answer2", "Answer3"), 3);
        doPost(q, status().isBadRequest());
    }

    @Test
    public void findAll() throws Exception {
        MvcResult result = doGet(status().isOk());
        // check returned result
        String stringResult = result.getResponse().getContentAsString();
        List all = new ObjectMapper().readValue(stringResult, new TypeReference<List<Question>>(){});
        Assert.assertEquals(q1, all.get(0));
        Assert.assertEquals(q2, all.get(1));
    }

    @Test
    public void findAllWithEmptyDB() throws Exception {
        questionRepo.deleteAll();
        doGet(status().isNotFound());
    }

    @Test
    public void findByExistentId() throws Exception {
        MvcResult result = doGet(q1.getId(), status().isOk());
        // check returned result
        String stringResult = result.getResponse().getContentAsString();
        Question qReturned = new ObjectMapper().readValue(stringResult, Question.class);
        Assert.assertNotNull(qReturned.getId());
        Assert.assertEquals(q1, qReturned);
    }

    @Test
    public void findByNonExistentId() throws Exception {
        questionRepo.deleteAll();
        doGet(q1.getId(), status().isNotFound());
    }

    @Test
    public void updateExistentQuestion() throws Exception {
        final String newAnswer = "newAnswer";
        Question q = new Question("q1", Arrays.asList("Answer1", "Answer2", "Answer3", newAnswer), 0);
        q.setId(q1.getId());
        doPut(q1.getId(), q, status().isNoContent());
        Question qSaved = questionRepo.findOne(q.getId());
        Assert.assertTrue(qSaved.getAnswers().contains(newAnswer));
    }

    @Test
    public void updateNonExistentQuestion() throws Exception {
        final String nonExistentId = "1234";
        Question q = new Question("q1", Arrays.asList("Answer1", "Answer2", "Answer3"), 0);
        q.setId(nonExistentId);
        doPut(nonExistentId, q, status().isNotFound());
    }

    @Test
    public void updateExistentQuestionWithDifferentId() throws Exception {
        final String nonExistentId = "1234";
        doPut(nonExistentId, q1, status().isBadRequest());
    }

    @Test
    public void deleteByExistentId() throws Exception {
        doDelete(q1.getId(), status().isNoContent());
    }

    @Test
    public void deleteByNonExistentId() throws Exception {
        questionRepo.deleteAll();
        doDelete(q1.getId(), status().isNotFound());
    }
}
