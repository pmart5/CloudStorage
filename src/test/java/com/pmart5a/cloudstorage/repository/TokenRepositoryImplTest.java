package com.pmart5a.cloudstorage.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.pmart5a.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenRepositoryImplTest {

    @InjectMocks
    private TokenRepositoryImpl tokenRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
        final var tokenAll = tokenRepository.getAllTokens();
        if (!tokenAll.isEmpty()) {
            for (final var token : tokenAll) {
                tokenRepository.removeToken(token);
            }
        }
        tokenRepository.addToken(TOKEN_ONE);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Nested
    public class methodAddTokenTest {

        @Test
        public void testAddTokenNewToken() {
            tokenRepository.addToken(TOKEN_TWO);

            final var actual = tokenRepository.getSizeTokens();

            assertEquals(NUMBER_TWO, actual);
        }

        @Test
        public void testAddTokenWhenTokenIsExist() {
            tokenRepository.addToken(TOKEN_ONE);

            final var actual = tokenRepository.getSizeTokens();

            assertEquals(NUMBER_ONE, actual);
        }
    }

    @Test
    public void testRemoveToken() {
        tokenRepository.removeToken(TOKEN_ONE);

        final var actual = tokenRepository.getSizeTokens();

        assertEquals(NUMBER_ZERO, actual);
    }

    @Nested
    public class methodIsTokenTest {

        @Test
        public void testIsTokenTrue() {
            final var actual = tokenRepository.isToken(TOKEN_ONE);

            assertTrue(actual);
        }

        @Test
        public void testIsTokenFalse() {
            final var actual = tokenRepository.isToken(TOKEN_TWO);

            assertFalse(actual);
        }
    }

    @Test
    public void testGetSizeTokens() {
        final var actual = tokenRepository.getSizeTokens();

        assertEquals(NUMBER_ONE, actual);
    }

    @Test
    public void testGetAllTokens() {
        final var expected = List.of(TOKEN_ONE);

        final var actual = tokenRepository.getAllTokens();

        assertEquals(expected, actual);
    }
}