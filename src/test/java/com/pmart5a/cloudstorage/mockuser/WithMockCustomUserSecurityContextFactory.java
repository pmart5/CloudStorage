package com.pmart5a.cloudstorage.mockuser;

import com.pmart5a.cloudstorage.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static com.pmart5a.cloudstorage.testdata.TestData.ID;
import static com.pmart5a.cloudstorage.testdata.TestData.PASSWORD;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = User.builder()
                .id(ID)
                .login(customUser.username())
                .password(PASSWORD)
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}