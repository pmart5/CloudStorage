package com.pmart5a.cloudstorage.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pmart5a.cloudstorage.generator.GeneratorId.getGeneratorId;
import static com.pmart5a.cloudstorage.utils.ApplicationData.CUSTOM_CONTENT_TYPE;
import static com.pmart5a.cloudstorage.utils.ApplicationData.FORMAT_MESSAGE;
import static com.pmart5a.cloudstorage.utils.ErrorMessages.UNAUTHORIZED_ERROR_MESSAGE;

@Component
@Slf4j
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (response.getStatus() != HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            final var errorId = getGeneratorId().getId();
            log.error("ErrorId: [{}]. TokenFilter. Token authentication. {}.", errorId, authException.getMessage());
            formResponse(response, errorId);
        }
    }

    protected void formResponse(HttpServletResponse response, Integer errorId) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CUSTOM_CONTENT_TYPE);
        response.getWriter().write(String.format(FORMAT_MESSAGE, UNAUTHORIZED_ERROR_MESSAGE, errorId));
    }
}