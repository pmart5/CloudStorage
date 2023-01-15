package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.model.User;
import com.pmart5a.cloudstorage.model.dto.AuthRequest;
import com.pmart5a.cloudstorage.model.dto.AuthResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private static final String VALUE_HEADER_AUTH_TOKEN = "Bearer tokenOne";
    private static final String TOKEN_ONE = "tokenOne";
    private static final String LOGIN = "test@mail.com";
    private static final String PASSWORD = "test";
    private static final Long ID = 1L;
    private static final User USER = User.builder()
            .id(ID)
            .login(LOGIN)
            .password(PASSWORD)
            .build();

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        when(userService.getUserAuth()).thenReturn(USER);
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testLoginSuccess() {
        final var authRequest = new AuthRequest(LOGIN, PASSWORD);
        final var expectedAuthResponse = new AuthResponse(TOKEN_ONE);
        when(tokenService.generateToken(USER)).thenReturn(TOKEN_ONE);

        final var actualAuthResponse = authService.login(authRequest);

        assertEquals(expectedAuthResponse, actualAuthResponse);
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD));
        verify(tokenService, times(1)).addTokenInStorage(TOKEN_ONE);
    }

    @Test
    public void testLogoutSuccess() {
        authService.logout(VALUE_HEADER_AUTH_TOKEN);

        verify(tokenService, times(1)).removeTokenFromStorage(TOKEN_ONE);
    }
}