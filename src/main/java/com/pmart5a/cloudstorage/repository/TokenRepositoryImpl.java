package com.pmart5a.cloudstorage.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private final static Set<String> tokens = new ConcurrentSkipListSet<>();

    public void addToken(String token) {
        tokens.add(token);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }

    public boolean isToken(String token) {
        return tokens.contains(token);
    }

    public int getSizeTokens() {
        return tokens.size();
    }

    public List<String> getAllTokens() {
        return tokens.stream().toList();
    }
}