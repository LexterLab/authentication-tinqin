package com.tinqinacademy.authentication.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.emails.api.operations.sendconfirmemail.SendConfirmEmailInput;
import com.tinqinacademy.emails.api.operations.sendconfirmemail.SendConfirmEmailOutput;
import com.tinqinacademy.emails.restexport.EmailClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailClient emailClient;

    @BeforeEach
     void setUp() {
       when(emailClient.sendConfirmEmail(any(SendConfirmEmailInput.class))).thenReturn(SendConfirmEmailOutput.builder()
               .build());
    }

    @Test
    void shouldRespondWithCreatedWhenRegistering() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .email("test@gmail.com")
                .username("test")
                .password("test1234")
                .confirmPassword("test1234")
                .firstName("Johnny")
                .lastName("Test")
                .phoneNo("+35987323232")
                .build();


        mockMvc.perform(post(RestAPIRoutes.REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRespondWithBadRequestWhenRegisteringExistingEmailUser() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .email("domino222@gmail.com")
                .username("test")
                .password("test1234")
                .confirmPassword("test1234")
                .firstName("Johnny")
                .lastName("Test")
                .phoneNo("+35987323232")
                .build();


        mockMvc.perform(post(RestAPIRoutes.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithBadRequestWhenRegisteringExistingUsernameUser() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .email("testmaster@gmail.com")
                .username("domino222")
                .password("test1234")
                .confirmPassword("test1234")
                .firstName("Johnny")
                .lastName("Test")
                .phoneNo("+35987323232")
                .build();


        mockMvc.perform(post(RestAPIRoutes.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithBadRequestWhenRegisteringWithWeakPassword() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .email("testmaster@gmail.com")
                .username("test")
                .password("test123")
                .confirmPassword("test1234")
                .firstName("Johnny")
                .lastName("Test")
                .phoneNo("+35987323232")
                .build();


        mockMvc.perform(post(RestAPIRoutes.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithBadRequestWhenRegisteringWithExistingPhoneNo() throws Exception {
        RegisterInput input = RegisterInput.builder()
                .email("testmaster@gmail.com")
                .username("test")
                .password("test1234")
                .confirmPassword("test1234")
                .firstName("Johnny")
                .lastName("Test")
                .phoneNo("+35984238424")
                .build();


        mockMvc.perform(post(RestAPIRoutes.REGISTER)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithOKWhenSigningIn() throws Exception {
        LoginInput input = LoginInput.builder()
                .username("domino222")
                .password("new2Password")
                .build();

        mockMvc.perform(post(RestAPIRoutes.LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondWithBadRequestWhenSigningInWhileUnverified() throws Exception {
        LoginInput input = LoginInput.builder()
                .username("domino")
                .password("password")
                .build();

        mockMvc.perform(post(RestAPIRoutes.LOGIN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithWhenSigningInWithBadCredentials() throws Exception {
        LoginInput input = LoginInput.builder()
                .username("domino222")
                .password("wrongpassword")
                .build();

        mockMvc.perform(post(RestAPIRoutes.LOGIN)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }
}