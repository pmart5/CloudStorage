package com.pmart5a.cloudstorage.advice;

import com.pmart5a.cloudstorage.exception.FileNameNotUniqueException;
import com.pmart5a.cloudstorage.exception.FileNotFoundException;
import com.pmart5a.cloudstorage.exception.NewFileNameNotUniqueException;
import com.pmart5a.cloudstorage.exception.NewFileNameUnknownException;
import com.pmart5a.cloudstorage.model.dto.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.pmart5a.cloudstorage.generator.GeneratorId.getGeneratorId;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileNameNotUniqueException.class)
    public ErrorMessageResponse handlerFileNameNotUnique(FileNameNotUniqueException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse("Ошибка ввода данных. Файл с таким именем уже есть в облаке.", errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileNotFoundException.class)
    public ErrorMessageResponse handlerFileNotFoundException(FileNotFoundException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse("Ошибка ввода данных. Файл с таким именем не найден.", errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewFileNameUnknownException.class)
    public ErrorMessageResponse handlerNewFileNameUnknown(NewFileNameUnknownException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse("Ошибка ввода данных. Отсутствует новое имя файла.", errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewFileNameNotUniqueException.class)
    public ErrorMessageResponse handlerNewFileNameNotUnique(NewFileNameNotUniqueException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse("Ошибка ввода данных. Новое имя файла совпадает с именем файла" +
                " в облаке.", errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessageResponse handlerAuthentication(AuthenticationException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. AuthService. Authentication. {}.", errorId, ex.getMessage());
        return new ErrorMessageResponse("Ошибка ввода данных. Неверные учётные данные.", errorId);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorMessageResponse handlerError(Exception ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse("Ошибка сервера. Попробуйте повторить операцию через какое-то время.",
                errorId);
    }
}