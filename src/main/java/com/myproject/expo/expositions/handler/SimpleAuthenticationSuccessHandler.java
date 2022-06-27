package com.myproject.expo.expositions.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final String ACCOUNT = "/home";
    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_USER = "USER";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        authorities.forEach(authority -> {
            if (authority.getAuthority().equals(ROLE_USER)) {
                redirectUserToRequiredURL(req, resp, "/user" + ACCOUNT);
            } else if (authority.getAuthority().equals(ROLE_ADMIN)) {
                redirectUserToRequiredURL(req, resp, "/admin" + ACCOUNT);
            }
        });
    }

    private void redirectUserToRequiredURL(HttpServletRequest req, HttpServletResponse resp, String url) {
        try {
            redirectStrategy.sendRedirect(req, resp, url);
        } catch (IOException e) {
            //TODO handle exception!!! ??
            e.printStackTrace();
        }
    }
}
