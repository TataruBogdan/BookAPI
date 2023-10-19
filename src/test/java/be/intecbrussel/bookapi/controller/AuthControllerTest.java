package be.intecbrussel.bookapi.controller;

import be.intecbrussel.bookapi.model.dto.LoginRequest;
import be.intecbrussel.bookapi.model.dto.LoginResponse;
import be.intecbrussel.bookapi.model.dto.RegisterRequest;
import be.intecbrussel.bookapi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login() throws Exception{
        //Arrange

        LoginRequest  loginRequest = new LoginRequest("gigi@email.com", "gigi");
        LoginResponse loginResponse = new LoginResponse("gigi@email.com", "token", "Admin");

        when(authService.login(any(LoginRequest.class))).thenReturn(Optional.of(loginResponse));

        //Act and Assert
        mockMvc.perform(
                    post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void register() throws Exception{

        //Arrange
        RegisterRequest loginRequest = new RegisterRequest("Didi", "Idid", "didi@email.com", "didi");

        when(authService.createUser(any(), any(), any(), any())).thenReturn(true);

        //Act and Assert
        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk());

    }
}