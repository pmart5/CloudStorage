package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.model.dto.AuthRequest;
import com.pmart5a.cloudstorage.model.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private static final int BEGIN_INDEX = 7;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthResponse login(AuthRequest authRequest) {
        final var username = authRequest.getLogin();
        final var password = authRequest.getPassword();
        log.info("Login attempt.");
        setAuthenticationUser(username, password);
        final var userAuth = userService.getUserAuth();
        log.info("The user with the ID [{}] has been granted access to the system.", userAuth.getId());
        final var authToken = tokenService.generateToken(userAuth);
        log.info("The user with the ID [{}] has been granted an access token.", userAuth.getId());
        tokenService.addTokenInStorage(authToken);
        log.info("The access token is placed in the storage.");
        return new AuthResponse(authToken);
    }

    public void logout(String authToken) {
        final var token = authToken.substring(BEGIN_INDEX);
        final var userId = userService.getUserAuth().getId();
        log.info("The user with the ID [{}] logged out.", userId);
        tokenService.removeTokenFromStorage(token);
        log.info("The user access token with the ID [{}] removed from storage.", userId);
    }

    private void setAuthenticationUser(String username, String password) {
        final var authentication = authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}