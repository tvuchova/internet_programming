package lesson_2.zadacha2_grades.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class StudentServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Starting the Student Server...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.out.println("Error with the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String command;

                while ((command = in.readLine()) != null) {
                    switch (command) {
                        case "ADD_STUDENT":
                            addStudent();
                            break;
                        case "VIEW_ALL_STUDENTS":
                            viewAllStudents();
                            break;
                        case "SEARCH_STUDENT":
                            searchStudent();
                            break;
                        case "VIEW_AVERAGE_GRADE":
                            viewAverageGrade();
                            break;
                        case "EXIT":
                            System.out.println("Client disconnected.");
                            return;
                        default:
                            out.println("Unknown command: " + command);
                    }
                    out.println("END");
                }
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void addStudent() throws IOException {
            String name = in.readLine();
            double grade = Double.parseDouble(in.readLine());

            StudentProcessor.addStudent(name, grade);
            out.println("Student " + name + " added successfully.");
        }

        private void viewAllStudents() {
            List<Student> students = StudentProcessor.readStudents();
            if (students.isEmpty()) {
                out.println("No students found.");
            } else {
                for (Student student : students) {
                    out.println("Student: " + student.getName() + " - Grade: " + student.getGrade());
                }
            }
        }

        private void searchStudent() throws IOException {
            String name = in.readLine();
            String result = StudentProcessor.searchStudent(name);
            out.println(result);
        }

        private void viewAverageGrade() {
            List<Student> students = StudentProcessor.readStudents();
            double average = StudentProcessor.calculateAverageGrade(students);
            out.println("Average grade: " + average);
        }
    }
}
