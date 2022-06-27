package com.myproject.expo.expositions.controller.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class LogoutFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(LogoutFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String servletPath = request.getServletPath();
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("USER") || (r.getAuthority().equals("ADMIN")));
        if ((servletPath.contains("/login") && hasUserRole)){
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            logger.info("User has role and pressed backward arrow!!!! MUST LOG OUT");
           //   response.sendRedirect();
        }
        logger.info("Logout filter working now  userRole {}",hasUserRole);
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
