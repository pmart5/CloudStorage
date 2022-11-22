package com.pmart5a.cloudstorage.service;

import com.pmart5a.cloudstorage.model.User;
import com.pmart5a.cloudstorage.model.entity.UserEntity;
import com.pmart5a.cloudstorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(user -> new User(
                        user.getId(),
                        user.getLogin(),
                        user.getPassword()
                ))
                .orElseThrow(() ->
//                new InputDataException("Некоректные учётные данные."));
        new UsernameNotFoundException("The user was not found in the database."));
    }

    public User getUserAuth() {
        final var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userAuth = null;
        if (principal instanceof User) {
            userAuth = (User) principal;
        }
        return userAuth;
    }

    public UserEntity getUserEntityFromUser(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .build();
    }
}