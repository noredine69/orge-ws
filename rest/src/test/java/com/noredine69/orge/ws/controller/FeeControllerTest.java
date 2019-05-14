package com.noredine69.orge.ws.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.noredine69.orge.ws.app.SprintBootApplication;
import com.noredine69.orge.ws.model.FeeRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SprintBootApplication.class)
@AutoConfigureMockMvc
public class FeeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getRootResources() throws Exception {

        mvc.perform( MockMvcRequestBuilders
                .post("/fee")
                .content(asJsonString(generateFeeRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fees").value("8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("spain or repeat"))
        ;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    FeeRequestDto generateFeeRequest() {
        FeeRequestDto feeRequestDto = new FeeRequestDto();


        return feeRequestDto;
    }
}
