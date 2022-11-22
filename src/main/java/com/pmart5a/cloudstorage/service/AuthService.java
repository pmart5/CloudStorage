package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.model.dto.AuthRequest;
import com.pmart5a.cloudstorage.model.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    private final UserService userService;

    public AuthResponse login(AuthRequest authRequest) {
        final var username = authRequest.getLogin();
        final var password = authRequest.getPassword();
        log.info("Login attempt.");
        Authentication authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var userAuth = userService.getUserAuth();
        log.info("The user with id [{}] has been granted access to the system.", userAuth.getId());
        final var authToken = tokenService.generateToken(userAuth);
        log.info("The user with id [{}] has been granted an access token.", userAuth.getId());
        tokenService.putTokenAndUserLoginInStorage(authToken, userAuth.getLogin());
        log.info("The access token is placed in the storage.");

        System.out.println("AuthService login: число токенов в хранилище: " + tokenService.getNumberOfRecordsInStorage());

        return new AuthResponse(authToken);

    }

    public void logout(String authToken) {
        final var token = authToken.substring(7);
        final var userId = userService.getUserAuth().getId();
        SecurityContextHolder.clearContext();
        log.info("The user with id [{}] logged out.", userId);
        tokenService.removeTokenFromStorage(token);
        log.info("The user access token with с id [{}] removed from storage.", userId);

        System.out.println("AuthService logout: число токенов в хранилище: " + tokenService.getNumberOfRecordsInStorage());

    }
}