package lesson_2.sockets_part3.finalized.grades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentClient {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) throws IOException {

        try {
            socket = new Socket("localhost", 9238);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Add student");
                System.out.println("2. View all students");
                System.out.println("3. Search student by name");
                System.out.println("4. Average mark for all students");
                System.out.println("5. Exit");
                System.out.print("Choose option (1-5): ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> addStudent(scanner);
                    case "2" -> viewAllStudents();
                    case "3" -> searchStudent(scanner);
                    case "4" -> viewAverageGrade();
                    case "5" -> {
                        out.println("EXIT"); // Send exit command to the server
                        System.out.println("Exit...");
                        scanner.close();
                        socket.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice.Try again");
                }
            }
        } catch (IOException e) {
            System.err.print(e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.print(e.getMessage());
            }
        }
    }

    private static void addStudent(Scanner scanner) throws IOException {
        String name;
        double grade;
        System.out.print("Enter student name: ");
        name = scanner.nextLine();
        System.out.print("Enter student grade: ");
        grade = scanner.nextDouble();

        out.println("add," + name + "," + grade);
    }

    private static void viewAllStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        String serverResponse;
        out.println("view");
        if ((serverResponse = in.readLine()) != null) {
            if(serverResponse.charAt(serverResponse.length() - 1) == ']') {
                serverResponse = serverResponse.substring(0, serverResponse.length() - 1);
            }
            if(serverResponse.charAt(0) == '[') {
                serverResponse = serverResponse.substring(1, serverResponse.length());
            }
            String[] allStudentStrings = serverResponse.split(", ");
            for (String student : allStudentStrings) {
                String[] parts = student.split(" - ");
                String name = parts[0];
                parts[1] = parts[1].replace("Оценка: ", "");
                double grade = Double.parseDouble(parts[1]);
                students.add(new Student(name, grade));
            }
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }


    private static void searchStudent(Scanner scanner) throws IOException {
        List<Student> students = new ArrayList<>();
        String name;
        System.out.print("Enter student name: ");
        name = scanner.nextLine();
        out.println("search," + name);

        String serverResponse;
        if ((serverResponse = in.readLine()) != null) {
            System.out.println(serverResponse);
        }
    }

    private static void viewAverageGrade() throws IOException {
        List<Student> students = new ArrayList<>();
        String serverResponse;
        out.println("view");
        if ((serverResponse = in.readLine()) != null) {
            if(serverResponse.charAt(serverResponse.length() - 1) == ']') {
                serverResponse = serverResponse.substring(0, serverResponse.length() - 1);
            }
            if(serverResponse.charAt(0) == '[') {
                serverResponse = serverResponse.substring(1, serverResponse.length());
            }
            String[] allStudentStrings = serverResponse.split(", ");
            for (String student : allStudentStrings) {
                String[] parts = student.split(" - ");
                String name = parts[0];
                parts[1] = parts[1].replace("Оценка: ", "");
                double grade = Double.parseDouble(parts[1]);
                students.add(new Student(name, grade));
            }
        }

        double avrgGrade = 0;

        for (Student student : students) {
            avrgGrade += student.getGrade();
        }

        System.out.println("Average grade: " + (avrgGrade / students.size()));
    }

    private static void readServerResponse() {

    }
}

