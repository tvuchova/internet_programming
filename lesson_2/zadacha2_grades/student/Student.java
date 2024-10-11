package lesson_2.zadacha2_grades.student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Student {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            String request;
            while (true) {
                System.out.println("Enter command (ADD <name> <grade>, VIEW, AVERAGE, or EXIT):");
                request = consoleInput.readLine();

                if ("EXIT".equalsIgnoreCase(request)) {
                    break;
                }

                out.println(request);

                String response;
                while ((response = in.readLine()) != null) {
                    if (response.equals("END")) {
                        break;
                    }
                    System.out.println("Server: " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
