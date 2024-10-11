package lesson_2.zadacha1.student;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Сървърът е стартиран и слуша на порт 1234...");

            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Нов клиент се свърза: " + clientSocket.getInetAddress().getHostAddress());

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.out.println("Грешка при стартиране на сървъра: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                System.out.println("Заявка от клиента: " + request);

                if (request.startsWith("ADD")) {
                    // Логика за добавяне на студент
                    String[] data = request.split(",");
                    String name = data[1];
                    int grade = Integer.parseInt(data[2]);
                    addStudent(name, grade);
                    out.println("Студентът е добавен успешно.");
                } else if (request.equals("LIST")) {
                    // Логика за връщане на списъка с оценки
                    String studentsList = getStudentsList();
                    out.println(studentsList);
                } else if (request.equals("AVERAGE")) {
                    // Логика за изчисляване на средна оценка
                    double average = calculateAverageGrade();
                    out.println("Средната оценка е: " + average);
                } else {
                    out.println("Невалидна команда.");
                }

            } catch (IOException e) {
                System.out.println("Грешка при обработка на клиента: " + e.getMessage());
            }
        }


        private void addStudent(String name, int grade) throws IOException {
            try (FileWriter fw = new FileWriter(FILE_NAME, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(name + "," + grade);
            }
        }


        private String getStudentsList() throws IOException {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        }


        private double calculateAverageGrade() throws IOException {
            int totalGrades = 0;
            int count = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    int grade = Integer.parseInt(data[1]);
                    totalGrades += grade;
                    count++;
                }
            }

            return count == 0 ? 0 : (double) totalGrades / count;
        }
    }
}
