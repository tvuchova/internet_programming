package org.example.lesson_2.task2_grades.teacher;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class StudentProcessor {
    private static final Logger LOGGER = Logger.getLogger(StudentProcessor.class.getName());

    private static final String FILE_NAME = "students.txt";

    public static void addStudent(String name, double grade) {
        synchronized (StudentProcessor.class) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(name + "," + grade);
                writer.newLine();
                LOGGER.info("Successfully added the student:" + name + "with mark:" + grade);
            } catch (IOException e) {
                LOGGER.severe("Error writing the data: {0}" + e.getMessage());
            }
        }
    }

    public static List<Student> readStudents() {
        List<Student> students = new ArrayList<>();
        synchronized (StudentProcessor.class) {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String name = parts[0];
                    double grade = Double.parseDouble(parts[1]);
                    students.add(new Student(name, grade));
                }
            } catch (IOException e) {
                LOGGER.severe("Error reading the data: {0}" + e.getMessage());
            }
        }
        return students;
    }

    public static String searchStudent(String name) {
        synchronized (StudentProcessor.class) {
            List<Student> students = readStudents();
            Optional<Student> student = students.stream()
                    .filter(st -> st.name().equalsIgnoreCase(name))
                    .findFirst();

            return student
                    .map(st -> "Student is found: " + st.name() + " with mark: " + st.grade())
                    .orElse("Student was not found.");
        }
    }

    public static double calculateAverageGrade(List<Student> students) {
        synchronized (StudentProcessor.class) {
            return students.stream()
                    .mapToDouble(Student::grade)
                    .average()
                    .orElse(0);
        }
    }
}
