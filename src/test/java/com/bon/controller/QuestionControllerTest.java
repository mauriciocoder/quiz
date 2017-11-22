package com.bon.controller;

import com.bon.ControllerBaseTest;
import com.bon.Application;
import com.bon.model.Question;
import com.bon.repository.QuestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, EmbeddedMongoAutoConfiguration.class})
@WebAppConfiguration
public class QuestionControllerTest extends ControllerBaseTest {

    @Autowired
    private QuestionRepository questionRepo;

    private Question q1, q2;

    @Before
    public void setup() throws Exception {
        questionRepo.deleteAll();
        q1 = questionRepo.save(Question
                .builder()
                .questioning("q1")
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3"))
                .correctAnswerIndex(0)
                .build());
        q2 = questionRepo.save(Question
                .builder()
                .questioning("q2")
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3"))
                .correctAnswerIndex(0)
                .build());
    }

    @Test
    public void should_create_valid_question() throws Exception {
        final Question q = Question
                .builder()
                .questioning("q")
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3"))
                .correctAnswerIndex(0)
                .build();
        final MvcResult postResult = doPost(q, status().isCreated());
        // check returned result
        String location = postResult.getResponse().getHeader("Location").toString();
        final MvcResult getResult = doGetByLocation(location, status().isOk());
        // check returned result
        String stringResult = getResult.getResponse().getContentAsString();
        Question qReturned = new ObjectMapper().readValue(stringResult, Question.class);
        Assert.assertNotNull(qReturned.getId());
        Assert.assertEquals(q, qReturned);
    }

    // TODO: Replicar o padrão de nomeação para os demais métodos
    @Test
    public void should_fail_with_400_when_question_is_duplicate() throws Exception {
        doPost(q1, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_DuplicatedQuestioning() throws Exception {
        final Question q = Question
                .builder()
                .questioning("q1")
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3"))
                .correctAnswerIndex(0)
                .build();
        doPost(q, status().isConflict());
    }

    @Test
    public void createInvalidQuestion_NullQuestioning() throws Exception {
        final Question q = Question
                .builder()
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3"))
                .correctAnswerIndex(0)
                .build();
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_EmptyAnswers() throws Exception {
        final Question q = Question
                .builder()
                .questioning("q")
                .answers(new ArrayList<>())
                .correctAnswerIndex(0)
                .build();
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_NullAnswers() throws Exception {
        final Question q = Question
                .builder()
                .questioning("q")
                .correctAnswerIndex(0)
                .build();
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_OutOfRangeCorrectAnswerIndex_below() throws Exception {
        final Question q = Question
                .builder()
                .questioning("q")
                .answers(new ArrayList<>())
                .correctAnswerIndex(-1)
                .build();
        doPost(q, status().isBadRequest());
    }

    @Test
    public void createInvalidQuestion_OutOfRangeCorrectAnswerIndex_after() throws Exception {
        final Question q = Question
                .builder()
                .questioning("q")
                .answers(new ArrayList<>())
                .correctAnswerIndex(3)
                .build();
        doPost(q, status().isBadRequest());
    }

    @Test
    public void findAll() throws Exception {
        MvcResult result = doGet(status().isOk());
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
        final Question q = Question
                .builder()
                .questioning("q1")
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3", newAnswer))
                .correctAnswerIndex(0)
                .build();
        q.setId(q1.getId());
        doPut(q1.getId(), q, status().isNoContent());
        Question qSaved = questionRepo.findOne(q.getId());
        Assert.assertTrue(qSaved.getAnswers().contains(newAnswer));
    }

    @Test
    public void updateNonExistentQuestion() throws Exception {
        final String nonExistentId = "1234";
        final Question q = Question
                .builder()
                .questioning("q1")
                .answers(Arrays.asList("Answer1", "Answer2", "Answer3"))
                .correctAnswerIndex(0)
                .build();
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
