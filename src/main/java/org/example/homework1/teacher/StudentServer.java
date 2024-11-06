package org.example.homework1.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;


public class StudentServer {
    private static final Logger LOGGER = Logger.getLogger(StudentServer.class.getName());
    private static final int MAX_CLIENTS = 20;
    private static final ExecutorService connectionPool = Executors.newFixedThreadPool(MAX_CLIENTS);

    public static void main(String[] args) {
        LoggerConfig.configureLogger(LOGGER);
        try (ServerSocket serverSocket = new ServerSocket(9998)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Client IP " + clientSocket.getInetAddress());

                //submit task to connection pool instead of new Thread(() -> handleClient(clientSocket)).start();
                connectionPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            // Shutdown the connection pool when the server is stopped
            shutdownConnectionPool();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String message;
            while ((message = in.readLine()) != null) {
                String[] parts = message.split(",");

                String command = parts[0].trim();

                switch (command) {
                    case "ADD" -> {
                        if (validateLength(parts, out)) continue;
                        String name = parts[1].trim();
                        double grade;
                        try {
                            grade = Double.parseDouble(parts[2].trim());
                        } catch (NumberFormatException e) {
                            out.println("Invalid mark.");
                            finishSend(out);
                            continue;
                        }
                        StudentProcessor.addStudent(name, grade);
                        out.println("Student was added successfully.");
                        finishSend(out);
                    }
                    case "VIEW" -> {
                        out.println(StudentProcessor.readStudents());
                        finishSend(out);
                    }
                    case "SEARCH" -> {
                        if (parts.length < 2) {
                            out.println("Invalid format.");
                            continue;
                        }
                        String searchName = parts[1].trim();
                        out.println(StudentProcessor.searchStudent(searchName));
                        finishSend(out);
                    }
                    case "AVERAGE" -> {
                        double average = StudentProcessor.calculateAverageGrade(StudentProcessor.readStudents());
                        out.println("Average mark for every student is: " + average);
                        finishSend(out);
                    }
                    default -> {
                        out.println("Invalid command.");
                    }
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private static void finishSend(PrintWriter out) {
        out.println("END");
    }

    private static void shutdownConnectionPool() {
        connectionPool.shutdown();
        try {
            if (!connectionPool.awaitTermination(60, SECONDS)) {
                connectionPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            connectionPool.shutdownNow();
        }
    }

    private static boolean validateLength(String[] parts, PrintWriter out) {
        if (parts.length < 3) {
            out.println("Invalid data format.");
            return true;
        }
        return false;
    }
}
