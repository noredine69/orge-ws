package com.noredine69.orge.ws.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.noredine69.orge.ws.app.SprintBootApplication;
import com.noredine69.orge.ws.model.FeeRequestDto;
import com.noredine69.orge.ws.model.IpDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${orge.ws.http.auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${orge.ws.http.auth-token}")
    private String principalRequestValue;

    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getRootResources() throws Exception {
        //@formatter:off
        this.mvc.perform(MockMvcRequestBuilders.post("/fee")
                .content(asJsonString(generateFeeRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .header(this.principalRequestHeader, this.principalRequestValue)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fees").value("10"));
        //@formatter:on
    }

    FeeRequestDto generateFeeRequest() {
        final IpDto clientIp = new IpDto();
        clientIp.setIp("217.127.206.227");
        final IpDto freelancerIp = new IpDto();
        freelancerIp.setIp("217.127.206.227");
        final FeeRequestDto feeRequestDto = new FeeRequestDto();
        feeRequestDto.client(clientIp).freelancer(freelancerIp);
        return feeRequestDto;
    }
}
