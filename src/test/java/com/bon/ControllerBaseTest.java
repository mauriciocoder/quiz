package com.bon;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class ControllerBaseTest {
    private final String requestPath;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    protected MockMvc mockMvc;

    public ControllerBaseTest() {
        try {
            this.requestPath = Class.forName(this.getClass().getName().replace("Test", "")).getAnnotation(RequestMapping.class).value()[0].toString();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void baseSetup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    protected MvcResult doGet(ResultMatcher expected) throws Exception {
        return mockMvc.perform(get(requestPath))
                .andExpect(expected)
                .andReturn();
    }

    protected MvcResult doGet(String resourceId, ResultMatcher expected) throws Exception {
        return mockMvc.perform(get(requestPath + "/" + resourceId))
                .andExpect(expected)
                .andReturn();
    }

    protected MvcResult doGetByLocation(String location, ResultMatcher expected) throws Exception {
        return mockMvc.perform(get(location))
                .andExpect(expected)
                .andReturn();
    }

    protected MvcResult doPost(Object obj, ResultMatcher expected) throws Exception {
        return mockMvc.perform(post(requestPath)
                .contentType(contentType)
                .content(json(obj)))
                .andExpect(expected)
                .andReturn();
    }

    protected MvcResult doPut(String resourceId, Object obj, ResultMatcher expected) throws Exception {
        return mockMvc.perform(put(requestPath + "/" + resourceId)
                .contentType(contentType)
                .content(json(obj)))
                .andExpect(expected)
                .andReturn();
    }

    protected MvcResult doDelete(String resourceId, ResultMatcher expected) throws Exception {
        return mockMvc.perform(delete(requestPath + "/" + resourceId))
                .andExpect(expected)
                .andReturn();
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Autowired
    public void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
}
