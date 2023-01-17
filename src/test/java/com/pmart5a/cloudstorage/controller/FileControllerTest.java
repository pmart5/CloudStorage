package com.pmart5a.cloudstorage.controller;

import com.pmart5a.cloudstorage.service.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

import static com.pmart5a.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private FileService fileService;
    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testUploadFileHttpStatusOkSuccess() throws IOException {
        final var actualResult = fileController.uploadFile(FILE_NAME, MULTIPART_FILE);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).uploadFile(FILE_NAME, MULTIPART_FILE);
    }

    @Test
    public void testDeleteFileHttpStatusOkSuccess() {
        final var actualResult = fileController.deleteFile(FILE_NAME);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).deleteFile(FILE_NAME);
    }

    @Test
    public void testDownloadFileHttpStatusOkSuccess() {
        when(fileService.downloadFile(FILE_NAME)).thenReturn(FILE_ENTITY);

        final var actualResult = fileController.downloadFile(FILE_NAME);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).downloadFile(FILE_NAME);
    }

    @Test
    public void testEditFileNameHttpStatusOkSuccess() {
        final var actualResult = fileController.editFileName(FILE_NAME, FILE_NAME_REQUEST);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).editFileName(FILE_NAME, NEW_FILE_NAME);
    }

    @Test
    public void testGetAllFilesHttpStatusOkSuccess() {
        when(fileService.getAllFiles(LIMIT)).thenReturn(List.of(FILE_RESPONSE));

        final var actualResult = fileController.getAllFiles(LIMIT);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).getAllFiles(LIMIT);
    }
}