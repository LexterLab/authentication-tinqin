package com.tinqinacademy.authentication.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.operations.finduser.FindUserInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRespondWithOKAndUserIdWhenFindingUserByPhoneNumber() throws Exception {
        FindUserInput input = FindUserInput.builder()
                .phoneNo("+35984238424")
                .build();

        mockMvc.perform(post(RestAPIRoutes.FIND_USER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("8eabb4ff-df5b-4e39-8642-0dcce375798c"));
    }

    @Test
    void shouldRespondWithNotFoundWhenFindingUserByUnknownPhoneNumber() throws Exception {
        FindUserInput input = FindUserInput.builder()
                .phoneNo("+35984238423")
                .build();

        mockMvc.perform(post(RestAPIRoutes.FIND_USER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }
}