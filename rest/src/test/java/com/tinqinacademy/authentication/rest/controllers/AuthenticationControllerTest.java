package com.tinqinacademy.authentication.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.authentication.api.RestAPIRoutes;
import com.tinqinacademy.authentication.api.exceptions.ResourceNotFoundException;
import com.tinqinacademy.authentication.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authentication.api.operations.getuser.GetUserInput;
import com.tinqinacademy.authentication.api.operations.login.LoginInput;
import com.tinqinacademy.authentication.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authentication.api.operations.register.RegisterInput;
import com.tinqinacademy.authentication.api.operations.resetpassword.ResetPasswordInput;
import com.tinqinacademy.authentication.persistence.crudrepositories.ConfirmationTokenRepository;
import com.tinqinacademy.authentication.persistence.crudrepositories.RecoveryTokenRepository;
import com.tinqinacademy.authentication.persistence.models.ConfirmationToken;
import com.tinqinacademy.authentication.persistence.models.RecoveryToken;
import com.tinqinacademy.emails.api.operations.sendconfirmemail.SendConfirmEmailInput;
import com.tinqinacademy.emails.api.operations.sendconfirmemail.SendConfirmEmailOutput;
import com.tinqinacademy.emails.restexport.EmailClient;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecoveryTokenRepository recoveryTokenRepository;

    @MockBean
    private ConfirmationTokenRepository confirmationTokenRepository;

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

    @Test
    void shouldRespondWithOKWhenRecoveringPassword() throws Exception {
        RecoverPasswordInput input = RecoverPasswordInput.builder()
                .email("domino222@gmail.com")
                .build();

        mockMvc.perform(post(RestAPIRoutes.RECOVER_PASSWORD)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondWithOKWhenResetPassword() throws Exception {
        RecoveryToken token = RecoveryToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(null)
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("8eabb4ff-df5b-4e39-8642-0dcce375798c")
                .build();

        ResetPasswordInput input = ResetPasswordInput
                .builder()
                .password("newPassword")
                .code(token.getValue())
                .build();

        when(recoveryTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.RESET_PASSWORD)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondWithBadRequestWhenResetPasswordWithWeakPassword() throws Exception {
        ResetPasswordInput input = ResetPasswordInput
                .builder()
                .password("pass")
                .code(UUID.randomUUID().toString())
                .build();


        mockMvc.perform(post(RestAPIRoutes.RESET_PASSWORD)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithBadRequestWhenResetPasswordWithAlreadyConfirmedToken() throws Exception {
        RecoveryToken token = RecoveryToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(LocalDateTime.now().toString())
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("8eabb4ff-df5b-4e39-8642-0dcce375798c")
                .build();

        ResetPasswordInput input = ResetPasswordInput
                .builder()
                .password("newPassword")
                .code(token.getValue())
                .build();

        when(recoveryTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.RESET_PASSWORD)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithNotFoundWhenResetPasswordWithNonExistentCode() throws Exception {

        ResetPasswordInput input = ResetPasswordInput
                .builder()
                .password("newPassword")
                .code(UUID.randomUUID().toString())
                .build();

        when(recoveryTokenRepository.findById(any(String.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(post(RestAPIRoutes.RESET_PASSWORD)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondWithNotFoundWhenResetPasswordWithUnknownUser() throws Exception {
        RecoveryToken token = RecoveryToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(null)
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("8eabb4ff-df5b-4e39-8642-0dcce375798a")
                .build();

        ResetPasswordInput input = ResetPasswordInput
                .builder()
                .password("newPassword")
                .code(token.getValue())
                .build();

        when(recoveryTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.RESET_PASSWORD)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondWithOKWhenConfirmingRegistration() throws Exception {
        ConfirmationToken token = ConfirmationToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(null)
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("1a419320-edf3-4b69-8f41-ce472f866a19")
                .build();

        ConfirmRegistrationInput input = ConfirmRegistrationInput
                .builder()
                .confirmationCode(UUID.randomUUID().toString())
                .build();

        when(confirmationTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.CONFIRM_REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespondWithBadRequestWhenConfirmingAlreadyConfirmedRegistration() throws Exception {
        ConfirmationToken token = ConfirmationToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(LocalDateTime.now().toString())
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("1a419320-edf3-4b69-8f41-ce472f866a19")
                .build();

        ConfirmRegistrationInput input = ConfirmRegistrationInput
                .builder()
                .confirmationCode(UUID.randomUUID().toString())
                .build();

        when(confirmationTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.CONFIRM_REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithBadRequestWhenConfirmingRegistrationOfAVerifiedUser() throws Exception {
        ConfirmationToken token = ConfirmationToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(null)
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("8eabb4ff-df5b-4e39-8642-0dcce375798c")
                .build();

        ConfirmRegistrationInput input = ConfirmRegistrationInput
                .builder()
                .confirmationCode(UUID.randomUUID().toString())
                .build();

        when(confirmationTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.CONFIRM_REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithNotFoundWhenConfirmingUnknownRegistrationCode() throws Exception {

        ConfirmRegistrationInput input = ConfirmRegistrationInput
                .builder()
                .confirmationCode(UUID.randomUUID().toString())
                .build();

        when(confirmationTokenRepository.findById(any(String.class))).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(post(RestAPIRoutes.CONFIRM_REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondWithNotFoundWhenConfirmingRegistrationOfAUnknownUser() throws Exception {
        ConfirmationToken token = ConfirmationToken
                .builder()
                .value(UUID.randomUUID().toString())
                .confirmedAt(null)
                .expirySeconds(900L)
                .createdAt(LocalDateTime.now().toString())
                .userId("8eabb4ff-df5b-4e39-8642-0dcce375798a")
                .build();

        ConfirmRegistrationInput input = ConfirmRegistrationInput
                .builder()
                .confirmationCode(UUID.randomUUID().toString())
                .build();

        when(confirmationTokenRepository.findById(any(String.class))).thenReturn(Optional.of(token));

        mockMvc.perform(post(RestAPIRoutes.CONFIRM_REGISTRATION)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRespondWithOKAndUserInfoWhenGettingUserInfo() throws Exception {
        GetUserInput input = GetUserInput
                .builder()
                .username("domino222")
                .build();

        mockMvc.perform(get(RestAPIRoutes.GET_USER, input.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("domino222"))
                .andExpect(jsonPath("$.email").value("domino222@gmail.com"));
    }

    @Test
    void shouldRespondWithBadRequestWhenGettingUserInfoWithBlankUsername() throws Exception {
        GetUserInput input = GetUserInput
                .builder()
                .username(" ")
                .build();

        mockMvc.perform(get(RestAPIRoutes.GET_USER, input.getUsername()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespondWithNotFoundWhenGettingUserInfoWithUnknownUsername() throws Exception {
        GetUserInput input = GetUserInput
                .builder()
                .username("test")
                .build();

        mockMvc.perform(get(RestAPIRoutes.GET_USER, input.getUsername()))
                .andExpect(status().isNotFound());
    }

}