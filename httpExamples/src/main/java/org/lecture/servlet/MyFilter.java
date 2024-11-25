package org.lecture.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * A simple filter that logs the request URI.
 */
@WebFilter(filterName = "MyFilter", urlPatterns = "/httpExamples/*")
@Slf4j
public class MyFilter implements Filter {
    public void init(FilterConfig config) {
        log.info("Initializing MyFilter");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        log.info("Request received for URI:{} ", httpRequest.getRequestURI());
        httpResponse.setHeader("X-My-Header", "MyFilter");

        chain.doFilter(request, response);

    }

    public void destroy() {
        log.info("Destroying MyFilter");
    }
}