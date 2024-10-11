package lesson_2.zadacha1.student;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        try {
            // Свързваме се със сървъра на локалната машина (localhost) и порт 1234
            Socket socket = new Socket("localhost", 1234);
            System.out.println("Свързване към сървъра...");

            // Изходен поток за изпращане на данни към сървъра
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Входен поток за четене на отговори от сървъра
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Вход от потребителя (конзолата)
            Scanner scanner = new Scanner(System.in);

            // Четене на командата от потребителя
            System.out.println("Изберете команда (ADD, LIST, AVERAGE): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("ADD")) {
                System.out.println("Въведете името на студента: ");
                String name = scanner.nextLine();
                System.out.println("Въведете оценката на студента: ");
                int grade = Integer.parseInt(scanner.nextLine());

                // Изпращаме команда за добавяне на студент
                out.println("ADD," + name + "," + grade);
            } else if (command.equalsIgnoreCase("LIST")) {
                // Изпращаме команда за преглед на списъка с оценки
                out.println("LIST");
            } else if (command.equalsIgnoreCase("AVERAGE")) {
                // Изпращаме команда за изчисляване на средна оценка
                out.println("AVERAGE");
            } else {
                System.out.println("Невалидна команда.");
                socket.close();
                return;
            }

            // Четене на отговорите от сървъра и показване на резултата на клиента
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Отговор от сървъра: " + response);
            }

            // Затваряме ресурсите
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Грешка при свързването със сървъра: " + e.getMessage());
        }
    }
}
