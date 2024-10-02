package lesson_2.sockets_part3.initial.grades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StudentServerThread implements Runnable {
    private final Socket clientSocket;
    public StudentServerThread(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)){
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message received from client: " + message);
                String[] messageParts = message.split(",");
                String command = messageParts[0].trim().toLowerCase();

                switch (command) {
                    case "add" -> {
                        //validate input
                        String name = messageParts[1].trim();
                        double grade = Double.parseDouble(messageParts[2].trim());
                        StudentProcessor.addStudent(name, grade);
                        out.println("Студентът е добавен успешно.");
                    }
                    case "view" -> out.println(StudentProcessor.readStudents());
                    case "search" -> {
                        String studentName = messageParts[1].trim();
                        out.println(StudentProcessor.searchStudent(studentName));
                    }
                    case "average" ->
                            out.println(StudentProcessor.calculateAverageGrade(StudentProcessor.readStudents()));
                    case "exit" -> {
                        System.out.println("Closing connection with client...");
                        return;
                    }
                    default -> out.println("Невалидна команда! Опитайте отново.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally{
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
