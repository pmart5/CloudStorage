package com.pmart5a.cloudstorage.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pmart5a.cloudstorage.generator.GeneratorId.getGeneratorId;

@Component
@Slf4j
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. TokenFilter. Token authentication. {}.", errorId, authException.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        String message;
        if (response.getStatus() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            message = "Ошибка сервера. Попробуйте повторить операцию через какое-то время.";
        } else {
            message = "Для доступа к этому ресурсу требуется полная аутентификация.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        response.getWriter().write(String.format("{\"message\":\"%s\",\"id\":%d}", message, errorId));
    }
}