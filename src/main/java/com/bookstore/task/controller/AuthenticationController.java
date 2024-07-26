package com.bookstore.task.controller;

import com.bookstore.task.configuration.jwt.JwtHelper;
import com.bookstore.task.controller.dto.AuthRequestDto;
import com.bookstore.task.controller.dto.AuthResponseDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;


    @PostMapping
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        var userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        var token = jwtHelper.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
