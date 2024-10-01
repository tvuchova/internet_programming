package lesson_2.sockets_part3.initial.grades;

import java.util.Scanner;

public class StudentClient {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n--- Меню ---");
                System.out.println("1. Добавяне на студент");
                System.out.println("2. Преглед на всички студенти");
                System.out.println("3. Търсене на студент по име");
                System.out.println("4. Средна оценка на всички студенти");
                System.out.println("5. Изход");
                System.out.print("Изберете опция (1-5): ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        addStudent(scanner);
                        break;
                    case "2":
                        viewAllStudents();
                        break;
                    case "3":
                        searchStudent(scanner);
                        break;
                    case "4":
                        viewAverageGrade();
                        break;
                    case "5":
                        System.out.println("Изход от програмата...");
                        return;
                    default:
                        System.out.println("Невалиден избор! Опитайте отново.");
                }
            }
        }
    }

    private static void sendMessage(String message) {

    }

    private static void addStudent(Scanner scanner) {

        //sendMessage("ADD," );
    }

    private static void viewAllStudents() {

    }

    private static void searchStudent(Scanner scanner) {

    }

    private static void viewAverageGrade() {

    }
}
