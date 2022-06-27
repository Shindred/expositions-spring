package com.myproject.expo.expositions.controller.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class LocaleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (req.getParameter("localeData") != null){
            String localeData = req.getParameter("localeData");
            String urlBack = req.getRequestURI();
            req.getSession().setAttribute("localeData",localeData);
            req.getRequestDispatcher(urlBack).forward(req,resp);
            return;
        }
      log.info("Locale filter");
      filterChain.doFilter(req,resp);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
