package com.bookstore.task.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bookstore.task.configuration.jwt.JwtHelper;
import com.bookstore.task.controller.dto.AuthRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthenticationController.class)
@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private UserDetailsService userDetailsService;

    private AuthenticationController authenticationController;

    private ObjectMapper objectMapper;
    private AuthRequestDto authRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        authenticationController = new AuthenticationController(authenticationManager, jwtHelper, userDetailsService);
        objectMapper = new ObjectMapper();
        authRequest = new AuthRequestDto("testuser", "testpassword");
        userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("testpassword")
                .authorities("ROLE_USER")
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void authenticate_shouldReturnToken_whenCredentialsAreCorrect() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(eq("testuser"))).thenReturn(userDetails);
        when(jwtHelper.generateToken(eq(userDetails))).thenReturn("testtoken");

        mockMvc.perform(post("/v1/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("testtoken"));
    }

    @Test
    void authenticate_shouldReturnUnauthorized_whenCredentialsAreIncorrect() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        mockMvc.perform(post("/v1/auth")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }
}


