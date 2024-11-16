package org.lecture.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        String userName = "Guest";
        int visitCount = 0;
        // Display user information


        response.setContentType("text/html");
        response.getWriter().println("<h1>Hello, " + userName + "</h1>");
        response.getWriter().println("<p>Site have been visited " + visitCount + " times.</p>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = "Guest";


        // Display user information
        response.setContentType("text/html");
        response.getWriter().println("<h1>Welcome back, " + userName + "!</h1>");
    }
}
