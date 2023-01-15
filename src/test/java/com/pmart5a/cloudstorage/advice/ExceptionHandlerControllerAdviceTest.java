package com.pmart5a.cloudstorage.advice;

import com.pmart5a.cloudstorage.exception.FileNameNotUniqueException;
import com.pmart5a.cloudstorage.exception.FileNotFoundException;
import com.pmart5a.cloudstorage.exception.NewFileNameNotUniqueException;
import com.pmart5a.cloudstorage.exception.NewFileNameUnknownException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerAdviceTest {

    private static final String ERROR_MESSAGE = "Error message";

    private static Stream<Arguments> sourceForHandlerException() {
        return Stream.of(Arguments.of(new AuthenticationServiceException(ERROR_MESSAGE)),
                Arguments.of(new Exception(ERROR_MESSAGE)));
    }

    @InjectMocks
    ExceptionHandlerControllerAdvice exceptionHandlerControllerAdvice;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testHandlerFileNameNotUniqueException() {
        final var exception = new FileNameNotUniqueException(ERROR_MESSAGE);
        final var expectedMessage = "Ошибка ввода данных. Файл с таким именем уже есть в облаке.";

        final var actualResponse = exceptionHandlerControllerAdvice.handlerFileNameNotUniqueException(exception);

        assertEquals(expectedMessage, actualResponse.getMessage());
    }

    @Test
    public void testHandlerFileNotFoundException() {
        final var expectedMessage = "Ошибка ввода данных. Файл с таким именем не найден.";
        final var exception = new FileNotFoundException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerFileNotFoundException(exception);

        assertEquals(expectedMessage, actualResponse.getMessage());
    }

    @Test
    public void testHandlerNewFileNameUnknownException() {
        final var expectedMessage = "Ошибка ввода данных. Отсутствует новое имя файла.";
        final var exception = new NewFileNameUnknownException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerNewFileNameUnknownException(exception);

        assertEquals(expectedMessage, actualResponse.getMessage());
    }

    @Test
    public void testHandlerNewFileNameNotUniqueException() {
        final var expectedMessage = "Ошибка ввода данных. Новое имя файла совпадает с именем файла в облаке.";
        final var exception = new NewFileNameNotUniqueException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerNewFileNameNotUniqueException(exception);

        assertEquals(expectedMessage, actualResponse.getMessage());
    }

    @Test
    public void testHandlerAuthenticationException() {
        final var expectedMessage = "Ошибка ввода данных. Неверные учётные данные.";
        final var exception = new BadCredentialsException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerAuthenticationException(exception);

        assertEquals(expectedMessage, actualResponse.getMessage());
    }

    @ParameterizedTest
    @MethodSource("sourceForHandlerException")
    public void testHandlerException(Exception exception) {
        final var expectedMessage = "Ошибка сервера. Попробуйте повторить операцию через какое-то время.";

        final var actualResponse = exceptionHandlerControllerAdvice.handlerException(exception);

        assertEquals(expectedMessage, actualResponse.getMessage());
    }
}