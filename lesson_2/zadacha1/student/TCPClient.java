package lesson_2.zadacha1.student;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        try {

            Socket socket = new Socket("localhost", 1234);
            System.out.println("Свързване към сървъра...");


            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);


            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            Scanner scanner = new Scanner(System.in);


            System.out.println("Изберете команда (ADD, LIST, AVERAGE): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("ADD")) {
                System.out.println("Въведете името на студента: ");
                String name = scanner.nextLine();
                System.out.println("Въведете оценката на студента: ");
                int grade = Integer.parseInt(scanner.nextLine());


                out.println("ADD," + name + "," + grade);
            } else if (command.equalsIgnoreCase("LIST")) {

                out.println("LIST");
            } else if (command.equalsIgnoreCase("AVERAGE")) {

                out.println("AVERAGE");
            } else {
                System.out.println("Невалидна команда.");
                socket.close();
                return;
            }


            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Отговор от сървъра: " + response);
            }

           
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Грешка при свързването със сървъра: " + e.getMessage());
        }
    }
}
