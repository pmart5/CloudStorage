package com.pmart5a.cloudstorage.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmart5a.cloudstorage.model.User;
import com.pmart5a.cloudstorage.model.dto.FileNameRequest;
import com.pmart5a.cloudstorage.model.dto.FileResponse;
import com.pmart5a.cloudstorage.model.entity.FileEntity;
import com.pmart5a.cloudstorage.model.entity.UserEntity;
import com.pmart5a.cloudstorage.repository.FileRepository;
import com.pmart5a.cloudstorage.repository.UserRepository;
import com.pmart5a.cloudstorage.service.TokenService;
import com.pmart5a.cloudstorage.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class FileIntegrationTest extends AbstractIntegrationTest {

    private static final String LOGIN = "test@mail.com";
    private static final String PASSWORD = "$2a$10$G.rPhcA1V8KRkVZ7yzVjwO0sGL1JYXR0fMp8gTLN3LkxXUKVD3Uf6";
    private static final String URL_FILE = "/cloud/file";
    private static final String URL_LIST = "/cloud/list";
    private static final String FILE_NAME = "test.txt";
    private static final String FILE_NAME_TWO = "tests.txt";
    private static final String NEW_FILE_NAME = "new.txt";
    private static final String NEW_FILE_NAME_EMPTY = " ";
    private static final String FILE_BODY = "Test content.";
    private static final Long FILE_SIZE = 13L;
    private static final String QUERY_PARAM_FILENAME = "filename";
    private static final String QUERY_PARAM_LIMIT = "limit";
    private static final String LIMIT = "3";
    private static final String BEARER = "Bearer ";
    private static final String AUTH_TOKEN_INVALID = "authTokenInvalid";
    private static final String AUTH_TOKEN_EMPTY = "";
    private static final String HEADER_AUTH_TOKEN = "auth-token";
    private static final MockMultipartFile MULTIPART_FILE =
            new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_BODY.getBytes());

    private static Stream<Arguments> sourceForErrorIsBadRequest() throws JsonProcessingException {
        final var contentNewFileNameEmpty = createContent(NEW_FILE_NAME_EMPTY);
        final var contentNewFileNameNew = createContent(NEW_FILE_NAME);
        final var contentNewFileNameDuplicate = createContent(FILE_NAME);
        return Stream.of(
                Arguments.of(
                        multipart(URL_FILE)
                                .file(MULTIPART_FILE)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                ),
                Arguments.of(
                        delete(URL_FILE)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME_TWO)
                ),
                Arguments.of(
                        get(URL_FILE)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME_TWO)
                ),
                Arguments.of(
                        put(URL_FILE)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                                .content(contentNewFileNameEmpty)
                                .contentType(MediaType.APPLICATION_JSON)
                ),
                Arguments.of(
                        put(URL_FILE)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME_TWO)
                                .content(contentNewFileNameNew)
                                .contentType(MediaType.APPLICATION_JSON)
                ),
                Arguments.of(
                        put(URL_FILE)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                                .content(contentNewFileNameDuplicate)
                                .contentType(MediaType.APPLICATION_JSON)
                )
        );
    }

    private static String createContent(String fileName) throws JsonProcessingException {
        final var fileNameRequest = new FileNameRequest(fileName);
        return new ObjectMapper().writeValueAsString(fileNameRequest);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    private String authToken;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
        userRepository.deleteAll();
        fileRepository.deleteAll();
        addTestUser();
        authToken = getAndSaveToken();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testUploadFileSuccess() throws Exception {
        mockMvc
                .perform(
                        multipart(URL_FILE)
                                .file(MULTIPART_FILE)
                                .header(HEADER_AUTH_TOKEN, BEARER + authToken)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFileSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, FILE_NAME);

        mockMvc
                .perform(
                        delete(URL_FILE)
                                .header(HEADER_AUTH_TOKEN, BEARER + authToken)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testDownloadFileSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, FILE_NAME);

        mockMvc
                .perform(
                        get(URL_FILE)
                                .header(HEADER_AUTH_TOKEN, BEARER + authToken)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testEditFileSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, FILE_NAME);
        final var fileNameRequest = new FileNameRequest(NEW_FILE_NAME);

        mockMvc
                .perform(
                        put(URL_FILE)
                                .header(HEADER_AUTH_TOKEN, BEARER + authToken)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                                .content(objectMapper.writeValueAsString(fileNameRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllFilesSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, FILE_NAME);
        addTestFile(testUser, FILE_NAME_TWO);
        final var list = fileRepository.findAllByUserId(testUser.getId()).stream()
                .map(file -> new FileResponse(file.getFileName(), file.getFileSize()))
                .collect(Collectors.toList());

        mockMvc
                .perform(
                        get(URL_LIST)
                                .header(HEADER_AUTH_TOKEN, BEARER + authToken)
                                .queryParam(QUERY_PARAM_LIMIT, LIMIT)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @ParameterizedTest
    @MethodSource("sourceForErrorIsBadRequest")
    public void testErrorIsBadRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, FILE_NAME);
        requestBuilder.header(HEADER_AUTH_TOKEN, BEARER + authToken);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {AUTH_TOKEN_EMPTY, AUTH_TOKEN_INVALID})
    public void testErrorIsUnauthorized(String token) throws Exception {
        final var testUser = getTestUser();
        tokenService.addTokenInStorage(AUTH_TOKEN_INVALID);
        addTestFile(testUser, FILE_NAME);

        mockMvc
                .perform(
                        delete(URL_FILE)
                                .header(HEADER_AUTH_TOKEN, BEARER + token)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testErrorIsInternalServerError() throws Exception {
        userRepository.deleteAll();

        mockMvc
                .perform(
                        multipart(URL_FILE)
                                .file(MULTIPART_FILE)
                                .header(HEADER_AUTH_TOKEN, BEARER + authToken)
                                .queryParam(QUERY_PARAM_FILENAME, FILE_NAME)
                )
                .andExpect(status().isInternalServerError());
    }

    private String getAndSaveToken() {
        final var testUser = getTestUser();
        final var authToken = tokenService.generateToken(testUser);
        tokenService.addTokenInStorage(authToken);
        return authToken;
    }

    private void addTestUser() {
        final var userEntity = UserEntity.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
        userRepository.save(userEntity);
    }

    private User getTestUser() {
        return userRepository.findByLogin(LOGIN)
                .map(user -> new User(
                        user.getId(),
                        user.getLogin(),
                        user.getPassword()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("The user was not found in the database."));
    }

    private void addTestFile(User testUser, String fileName) {
        final var fileEntity = FileEntity.builder()
                .fileName(fileName)
                .fileSize(FILE_SIZE)
                .fileType(MediaType.TEXT_PLAIN_VALUE)
                .fileDateUpdate(LocalDateTime.now())
                .fileByte(FILE_BODY.getBytes())
                .user(userService.getUserEntityFromUser(testUser))
                .build();
        fileRepository.save(fileEntity);
    }
}