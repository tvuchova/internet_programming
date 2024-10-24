package org.example.lesson_2.task2_grades.teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class StudentClient {
    private static final Logger LOGGER = Logger.getLogger(StudentClient.class.getName());

    public static void main(String[] args) {
        LoggerConfig.configureLogger(LOGGER);

        try (Socket socket = new Socket("127.0.0.1", 9998);
             Scanner scanner = new Scanner(System.in);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
                LOGGER.info("\n--- Menu ---");
                LOGGER.info("1. Add student");
                LOGGER.info("2. View all students");
                LOGGER.info("3. Search student by name");
                LOGGER.info("4. Average mark for all students");
                LOGGER.info("5. Exit");
                LOGGER.info("Choose option (1-5): ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        addStudent(scanner, out, in);
                        break;
                    case "2":
                        viewAllStudents(out, in);
                        break;
                    case "3":
                        searchStudent(scanner, out, in);
                        break;
                    case "4":
                        viewAverageGrade(out, in);
                        break;
                    case "5":
                        LOGGER.info("Exiting the program...");
                        return;
                    default:
                        LOGGER.info("Invalid choice. Try again!");
                        break;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Connection error: " + e.getMessage());
        }
    }

    private static void sendMessage(String message, PrintWriter out, BufferedReader in) {
        try {
            out.println(message);
            String response;
            while ((response = in.readLine()) != null) {
                LOGGER.info(response);
                if ("END".equalsIgnoreCase(response)) {
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error sending message: " + e.getMessage());
        }
    }

    private static void addStudent(Scanner scanner, PrintWriter out, BufferedReader in) {
        LOGGER.info("Enter the name of student: ");
        String name = scanner.nextLine().trim();
        LOGGER.info("Enter the student mark: ");
        double grade;
        try {
            grade = Double.parseDouble(scanner.nextLine());
            sendMessage("ADD," + name + "," + grade, out, in); // you will need to modify sendMessage
        } catch (NumberFormatException e) {
            LOGGER.severe("Invalid mark. Please enter a valid number for the grade.");
        }
    }

    private static void viewAllStudents(PrintWriter out, BufferedReader in) {
        sendMessage("VIEW", out, in);
    }

    private static void searchStudent(Scanner scanner, PrintWriter out, BufferedReader in) {
        LOGGER.info("Enter the name of student for search: ");
        String name = scanner.nextLine().trim();
        sendMessage("SEARCH," + name, out, in);
    }

    private static void viewAverageGrade(PrintWriter out, BufferedReader in) {
        sendMessage("AVERAGE", out, in);
    }
}
