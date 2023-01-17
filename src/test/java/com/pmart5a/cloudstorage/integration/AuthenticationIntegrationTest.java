package com.pmart5a.cloudstorage.integration;

import com.pmart5a.cloudstorage.model.dto.AuthRequest;
import com.pmart5a.cloudstorage.service.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.util.stream.Stream;

import static com.pmart5a.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationIntegrationTest extends AbstractIntegrationTest {

    private static Stream<Arguments> sourceForLoginInvalid() {
        return Stream.of(Arguments.of(INVALID_VALUE, PASSWORD),
                Arguments.of(LOGIN, INVALID_VALUE),
                Arguments.of(INVALID_VALUE, INVALID_VALUE));
    }

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    @Sql({"classpath:delete_from_users.sql", "classpath:insert_user_users.sql"})
    public void testLoginSuccess() {
        final var authRequest = new AuthRequest(LOGIN, PASSWORD);
        final var requestEntity = new RequestEntity<>(authRequest, HttpMethod.POST, URI.create(URL_LOGIN));
        final var responseEntity = restTemplate.exchange(URL_LOGIN, HttpMethod.POST, requestEntity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @ParameterizedTest
    @MethodSource("sourceForLoginInvalid")
    @Sql({"classpath:delete_from_users.sql", "classpath:insert_user_users.sql"})
    public void testLoginInvalid(String login, String password) {
        final var authRequest = new AuthRequest(login, password);
        final var requestEntity = new RequestEntity<>(authRequest, HttpMethod.POST, URI.create(URL_LOGIN));
        final var responseEntity = restTemplate.exchange(URL_LOGIN, HttpMethod.POST, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testLogoutSuccess() {
        final var authToken = tokenService.generateToken(USER_ONLY_ID);
        tokenService.addTokenInStorage(authToken);
        final var headers = new HttpHeaders();
        final var headerValue = BEARER + authToken;
        headers.add(HEADER_AUTH_TOKEN, headerValue);
        final var requestEntity = new RequestEntity<>(headers, HttpMethod.POST, URI.create(URL_LOGOUT));
        final var responseEntity = restTemplate.exchange(URL_LOGOUT, HttpMethod.POST, requestEntity, Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}