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

        // Log request URI
       
        // Add a custom header to response

        // Pass the request along the filter chain
        chain.doFilter(request, response);

        // Adding a comment or additional logging after the chain.doFilter call
        log.info("Response processed for URI: " + httpRequest.getRequestURI());
    }

    public void destroy() {
        log.info("Destroying MyFilter");
    }
}