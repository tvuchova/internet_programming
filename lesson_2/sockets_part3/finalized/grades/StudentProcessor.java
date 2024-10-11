package lesson_2.sockets_part3.finalized.grades;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lesson_2.sockets_part3.initial.grades.Student;

public class StudentProcessor {

    private static final String FILE_NAME = "students.txt";

    public static void addStudent(String name, double grade) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(name + "," + grade);
            writer.newLine();

            System.out.println("Student is successfully added: " + name + " with mark: " + grade);
        } catch (IOException e) {
            System.out.println("Error while writing the data: " + e.getMessage());
        }
    }

    public static List<Student> readStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double grade = Double.parseDouble(parts[1]);
                students.add(new Student(name, grade));
            }
        } catch (IOException e) {
            System.out.println("Error reading the data: " + e.getMessage());
        }
        return students;
    }

    public static String searchStudent(String name) {
        List<Student> students = readStudents();
        Optional<Student> student = students.stream()
                .filter(st -> st.getName().equalsIgnoreCase(name))
                .findFirst();

        return student
                .map(st -> "Found student: " + st.getName() + " with mark: " + st.getGrade())
                .orElse("The student is no found.");
    }

    public static double calculateAverageGrade(List<Student> students) {
        return students.stream()
                .mapToDouble(Student::getGrade)
                .average()
                .orElse(0);
    }
}
