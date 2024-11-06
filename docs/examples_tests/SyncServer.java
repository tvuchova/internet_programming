package org.example.examples;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server is listening on port 8888...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Connected to client!");

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    
                    String clientMessage = in.readLine();
                    System.out.println("Received message from client: " + clientMessage);

                    if (clientMessage.equalsIgnoreCase("hello")) {
                        out.println("Welcome to the server!");
                    } else {
                        out.println("Unknown message received.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

