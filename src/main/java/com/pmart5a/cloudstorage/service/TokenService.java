package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.model.User;
import com.pmart5a.cloudstorage.repository.TokenRepositoryImpl;
import com.pmart5a.cloudstorage.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepositoryImpl tokenRepository;
    private final TokenUtil tokenUtil;

    public void putTokenAndUserLoginInStorage(String token, String login) {
        tokenRepository.putTokenAndUserLogin(token, login);
    }

    public void removeTokenFromStorage(String token) {
        tokenRepository.removeToken(token);
    }

    public String getUserLoginFromStorage(String token) {
        return tokenRepository.getUserLoginByToken(token);
    }

    public boolean isTokenInStorage(String token) {
        return tokenRepository.isToken(token);
    }

    public int getNumberOfRecordsInStorage() {
        return tokenRepository.getSizeTokens();
    }

    public Set<String> getAllTokensFromStorage() {
        return tokenRepository.putAllTokens();
    }

    public String generateToken(User user) {
        return tokenUtil.generateToken(user);
    }

    public boolean checkToken(String token) {
        return tokenUtil.checkToken(token);
    }

    @Scheduled(fixedDelayString = "${jwt.token.check-interval}")
    public void clearTheStorageOfInvalidTokens() {
        log.info("The operation of deleting invalid access tokens from the storage has been started." +
                " Total tokens in storage: [{}].", getNumberOfRecordsInStorage());
        final var tokenAll = getAllTokensFromStorage();
        int numberOfDeletedTokens = 0;
        if (!tokenAll.isEmpty())
            for (final var token : tokenAll) {
                if (!checkToken(token)) {
                    removeTokenFromStorage(token);
                    numberOfDeletedTokens++;
                }
            }
        log.info("The operation of deleting invalid access tokens from the storage has been completed." +
                " Deleted: [{}].", numberOfDeletedTokens);
    }
}