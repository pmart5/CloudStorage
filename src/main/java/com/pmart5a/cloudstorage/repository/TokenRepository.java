package com.pmart5a.cloudstorage.repository;

public interface TokenRepository {

    void putTokenAndUserLogin(String token, String login);

    void removeToken(String token);

    String getUserLoginByToken(String token);

    boolean isToken(String token);

    int getSizeTokens();
}