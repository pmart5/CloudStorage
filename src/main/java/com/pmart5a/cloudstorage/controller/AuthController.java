package com.pmart5a.cloudstorage.controller;

import com.pmart5a.cloudstorage.model.dto.AuthRequest;
import com.pmart5a.cloudstorage.model.dto.AuthResponse;
import com.pmart5a.cloudstorage.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/cloud")
public class AuthController {

    private final AuthService authService;

    @PostMapping( "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("auth-token") String authToken) {
        authService.logout(authToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}