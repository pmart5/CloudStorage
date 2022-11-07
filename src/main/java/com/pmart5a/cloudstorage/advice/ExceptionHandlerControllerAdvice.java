package com.pmart5a.cloudstorage.advice;

import com.pmart5a.cloudstorage.exception.FileOperationsException;
import com.pmart5a.cloudstorage.exception.InputDataException;
import com.pmart5a.cloudstorage.exception.UnauthorizedException;
import com.pmart5a.cloudstorage.model.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InputDataException.class)
    public ErrorMessageResponse HandlerInputData (InputDataException ex) {
        return new ErrorMessageResponse(ex.getMessage(), 400);
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorMessageResponse HandlerUnauthorized (UnauthorizedException ex) {
        return new ErrorMessageResponse(ex.getMessage(), 401);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FileOperationsException.class)
    public ErrorMessageResponse HandlerFile (FileOperationsException ex) {
        return new ErrorMessageResponse(ex.getMessage(), 500);
    }
}