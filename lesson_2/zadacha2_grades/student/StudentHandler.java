package lesson_2.zadacha2_grades.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.*;
class StudentHandler extends Thread {
    private final Socket StudentSocket;

    public StudentHandler(Socket socket) {
        this.StudentSocket = socket;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(StudentSocket.getInputStream()));
            PrintWriter out = new PrintWriter(StudentSocket.getOutputStream(), true);

            String request;
            while ((request = in.readLine()) != null) {
                String[] requestData = request.split(" ");
                String command = requestData[0];

                if ("ADD".equalsIgnoreCase(command)) {
                    String name = requestData[1];
                    double grade = Double.parseDouble(requestData[2]);
                    StudentServer.addStudent(name, grade);
                    out.println("Student successfully added.");
                } else if ("VIEW".equalsIgnoreCase(command)) {
                    List<String> students = StudentServer.getAllGrades();
                    if (students.isEmpty()) {
                        out.println("No grades available.");
                    } else {
                        for (String student : students) {
                            out.println(student);
                        }
                        out.println("");
                    }
                } else if ("AVERAGE".equalsIgnoreCase(command)) {
                    double average = StudentServer.calculateAverageGrade();
                    out.println("The average grade is: " + average);
                } else {
                    out.println("Invalid command.");
                }
                out.println("END");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
