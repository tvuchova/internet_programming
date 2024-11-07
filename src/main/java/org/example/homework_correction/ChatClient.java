package org.example.homework_correction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        System.out.println("Connecting to the chat server...");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the chat server. Type 'exit' to leave the chat.");

            Thread listenerThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Connection to server lost.");
                }
            });
            listenerThread.start();

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                out.println(message);

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Exiting chat...");
                    break;
                }
            }

            listenerThread.join();

        } catch (IOException e) {
            System.err.println("Unable to connect to the chat server: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Client interrupted.");
        }
    }
}
