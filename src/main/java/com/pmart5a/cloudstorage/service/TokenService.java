package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.model.User;
import com.pmart5a.cloudstorage.repository.TokenRepositoryImpl;
import com.pmart5a.cloudstorage.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

//    private static final int MAX_NUMBER_OF_RECORDS = 1000;
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

    public String generateToken(User user) {
        return tokenUtil.generateToken(user);
    }

    public boolean checkToken(String token) {
        return tokenUtil.checkToken(token);
    }

//    public void clearTheStorageOfOldTokens() {
//        if (tokenRepository.getSizeTokens() > MAX_NUMBER_OF_RECORDS) {
//
//        }
//    }

}
