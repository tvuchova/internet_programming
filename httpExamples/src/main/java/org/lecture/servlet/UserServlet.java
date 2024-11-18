package org.lecture.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/*
 * Servlet Filter: Logs incoming requests and responses, and adds a header to the response.

 * HttpSession: Tracks a user’s session and counts page visits.

 * Cookies: Uses cookies to store the user’s name and display it across requests.

 * */
@WebServlet("/UserServlet")
@Slf4j
public class UserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("username");
        HttpSession session = request.getSession();
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) {
            visitCount = 1;
        } else {
            visitCount++;
        }
        session.setAttribute("visitCount", visitCount);

        Cookie userCookie = new Cookie("userName", userName);
        userCookie.setMaxAge(60 * 60 * 24); // 1 day
        response.addCookie(userCookie);

        response.setContentType("text/html");
        response.getWriter().println("<h1>Hello, " + userName + "</h1>");
        response.getWriter().println("<p>Site have been visited " + visitCount + " times.</p>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = "Guest";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userName".equals(cookie.getName())) {
                    userName = cookie.getValue();
                    break;
                }
            }
        }

        // Display user information
        response.setContentType("text/html");
        response.getWriter().println("<h1>Welcome back, " + userName + "!</h1>");
    }
}
