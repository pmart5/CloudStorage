package com.pmart5a.cloudstorage.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private final static Map<String, String> tokens = new ConcurrentHashMap<>();

    public void putTokenAndUserLogin(String token, String login) {
        tokens.put(token, login);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    public String getUserLoginByToken(String token) {
        return tokens.get(token);
    }

    public boolean isToken(String token) {
        return tokens.containsKey(token);
    }

    public int getSizeTokens() {
        return tokens.size();
    }

//    public void putAllTokens() {
//        tokens.keySet().;
//    }
}