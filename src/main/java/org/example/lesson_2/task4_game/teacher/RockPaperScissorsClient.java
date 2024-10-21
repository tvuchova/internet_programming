package org.example.lesson_2.task4_game.teacher;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class RockPaperScissorsClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- Choice ---");
                System.out.println("1. Rock");
                System.out.println("2. Scissors");
                System.out.println("3. Paper");
                System.out.println("4. Exit");
                System.out.print("ChoseOption (1-4): ");

                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    sendMessage(socket, serverAddress, serverPort, "Rock");
                } else if (choice.equals("2")) {
                    sendMessage(socket, serverAddress, serverPort, "Scissors");
                } else if (choice.equals("3")) {
                    sendMessage(socket, serverAddress, serverPort, "Paper");
                } else if (choice.equals("4")) {
                    System.out.println("Exit of game...");
                    break;
                } else {
                    System.out.println("Invalid choice.Try again!");
                    continue;
                }

                String response = receiveMessage(socket);
                System.out.println("Server response: " + response);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void sendMessage(DatagramSocket socket, String serverAddress, int serverPort, String message) {
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, java.net.InetAddress.getByName(serverAddress), serverPort);
            socket.send(packet);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String receiveMessage(DatagramSocket socket) {
        try {
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            return new String(packet.getData(), 0, packet.getLength());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
