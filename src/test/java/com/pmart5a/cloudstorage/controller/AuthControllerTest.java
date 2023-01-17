package com.pmart5a.cloudstorage.controller;

import com.pmart5a.cloudstorage.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static com.pmart5a.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testLoginHttpStatusOkSuccess() {
        when(authService.login(AUTH_REQUEST)).thenReturn(AUTH_RESPONSE);

        final var actualResult = authController.login(AUTH_REQUEST);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(authService, times(1)).login(AUTH_REQUEST);
    }

    @Test
    public void testLogoutHttpStatusOkSuccess() {
        final var actualResult = authController.logout(TOKEN_ONE);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(authService, times(1)).logout(TOKEN_ONE);
    }
}