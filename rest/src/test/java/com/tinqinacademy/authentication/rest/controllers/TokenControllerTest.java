package com.tinqinacademy.authentication.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenInput;
import com.tinqinacademy.authentication.api.operations.generateaccesstoken.GetUsernameFromTokenOutput;
import com.tinqinacademy.authentication.core.jwt.JWTTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @Test
    void shouldRespondWithOKAndUsernameWhenGettingUsernameFromToken() throws Exception {
        GetUsernameFromTokenInput input = GetUsernameFromTokenInput
                .builder()
                .token("token")
                .build();

        GetUsernameFromTokenOutput output = GetUsernameFromTokenOutput
                .builder()
                .username("domino222")
                .build();

        when(jwtTokenProvider.getUsername(input.getToken())).thenReturn(output.getUsername());

        mockMvc.perform(post(RestAPIRoutes.GET_USER_FROM_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(output.getUsername()));
    }

}