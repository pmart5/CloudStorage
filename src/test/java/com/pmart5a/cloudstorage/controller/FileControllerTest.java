package com.pmart5a.cloudstorage.controller;

import com.pmart5a.cloudstorage.model.dto.FileNameRequest;
import com.pmart5a.cloudstorage.model.dto.FileResponse;
import com.pmart5a.cloudstorage.model.entity.FileEntity;
import com.pmart5a.cloudstorage.service.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    private static final String FILE_NAME = "test.txt";
    private static final String NEW_FILE_NAME = "new.txt";
    private static final String FILE_BODY = "Test content.";
    private static final String FILE_TYPE = "text/plain";
    private static final Long FILE_SIZE = 13L;
    private static final Long ID = 1L;
    private static final Integer LIMIT = 3;
    private static final FileNameRequest FILE_NAME_REQUEST = new FileNameRequest(NEW_FILE_NAME);
    private static final FileResponse FILE_RESPONSE = new FileResponse(FILE_NAME, FILE_SIZE);
    private static final MockMultipartFile MULTIPART_FILE =
            new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_BODY.getBytes());
    private static final FileEntity FILE_ENTITY = FileEntity.builder()
            .id(ID)
            .fileName(FILE_NAME)
            .fileSize(FILE_SIZE)
            .fileType(FILE_TYPE)
            .build();

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