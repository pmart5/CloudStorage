package com.pmart5a.cloudstorage.security;

import com.pmart5a.cloudstorage.service.TokenService;
import com.pmart5a.cloudstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pmart5a.cloudstorage.generator.GeneratorId.getGeneratorId;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private static final String AUTH_TOKEN = "auth-token";
    private static final int BEGIN_INDEX = 7;

    private final TokenService tokenService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authToken = getTokenFromRequest(request);
        if (authToken != null && tokenService.isTokenInStorage(authToken)) {
            if (tokenService.checkToken(authToken)) {
                final var username = tokenService.getUserLoginFromStorage(authToken);
                try {
                    final var userDetails = userService.loadUserByUsername(username);
                    final var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } catch (Exception ex) {
                    final var errorId = getGeneratorId().getId();
                    log.error("ErrorId: [{}]. TokenFilter. {}", errorId, ex.getMessage());
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                tokenService.removeTokenFromStorage(authToken);
                log.info("The access token has been removed from storage.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final var authToken = request.getHeader(AUTH_TOKEN);
        if (StringUtils.hasText(authToken) && authToken.startsWith("Bearer ")) {
            return authToken.substring(BEGIN_INDEX);
        }
        return null;
    }
}