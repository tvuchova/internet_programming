# UserServlet and MyFilter Description

## Overview

This Java application, implemented as a servlet with the `@WebServlet("/UserServlet")` annotation, handles user
sessions, cookies, and request tracking for a basic web page. It consists of:

- **UserServlet**: Manages user interactions via `POST` and `GET` requests.
- **MyFilter**: A servlet filter that logs request URIs, adds custom headers, and demonstrates filter chaining.

## Code Components

### 1. `UserServlet` Class

The `UserServlet` class extends `HttpServlet` and is responsible for handling user requests. It includes:

- **Session Tracking**: Tracks page visits by incrementing a counter stored in the `HttpSession`.
- **Cookie Management**: Stores and retrieves the username in a cookie to maintain a personalized experience.
- **Logging**: Logs user information for debugging purposes.

#### Methods

- **doPost(HttpServletRequest request, HttpServletResponse response)**
    - **Request Parameter**: Retrieves the `username` from the request.
    - **Session Management**: Retrieves or initializes the `visitCount` from `HttpSession`, increments it, and saves it
      back to the session.
    - **Cookie Storage**: Creates a cookie named `userName` to store the username for one day.
    - **Response Display**: Displays a personalized message showing the user's name and visit count.

- **doGet(HttpServletRequest request, HttpServletResponse response)**
    - **Cookie Retrieval**: Reads cookies to retrieve the username if stored previously.
    - **Response Display**: Greets the user with a personalized message, defaulting to "Guest" if no `userName` cookie
      is found.

### 2. `MyFilter` Class

The `MyFilter` class, implemented as a servlet filter, provides request logging and header management.

#### Methods

- **doFilter(ServletRequest request, ServletResponse response, FilterChain chain)**
    - **Logging**: Logs the URI of incoming requests.
    - **Custom Header**: Adds a custom header `X-Processed-By` with a value of `"MyFilter"` to the response.
    - **Filter Chaining**: Passes the request and response along the filter chain, allowing other filters and the
      servlet to process the request.
    - **Post-Processing**: Logs an additional message after processing by the filter chain.

- **destroy()**
    - **Logging**: Logs a message when the filter is destroyed.

## Example Flow

### POST Request to `/UserServlet`

1. User submits a form with `username`.
2. `doPost()` stores `username` in a cookie and tracks the visit count in the session.
3. Displays a message showing the user's name and visit count.

### GET Request to `/UserServlet`

1. If `userName` cookie is available, `doGet()` retrieves the username and greets the user.
2. If no cookie is found, displays "Welcome back, Guest!"

### Filter Processing

1. Logs each request URI.
2. Adds a custom header to responses.
3. Allows other filters or servlets to handle the request.

## Key Features

- **Session Handling**: Tracks user visit count using `HttpSession`.
- **Cookies**: Saves username in a cookie for later retrieval.
- **Filter Chaining**: Demonstrates pre- and post-processing of requests.
- **Logging**: Logs key information to assist in debugging.
