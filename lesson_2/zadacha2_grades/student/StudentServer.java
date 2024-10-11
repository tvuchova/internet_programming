package lesson_2.zadacha2_grades.student;

import java.io.*;
import java.net.*;
import java.util.*;

public class StudentServer {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                new StudentHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void addStudent(String name, double grade) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(name + "," + grade);
            writer.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized List<String> getAllGrades() throws IOException {
        List<String> students = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists() || file.length() == 0) {
            return students;
        }

        try{
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static synchronized double calculateAverageGrade() throws IOException {
        List<String> students = getAllGrades();
        if (students.isEmpty()) {
            return 0.0;
        }

        double total = 0;
        int count = 0;
        for (String student : students) {
            String[] data = student.split(",");
            double grade = Double.parseDouble(data[1]);
            total += grade;
            count++;
        }
        return total / count;
    }
}