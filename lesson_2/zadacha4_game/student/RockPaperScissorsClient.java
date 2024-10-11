package lesson_2.zadacha4_game.student;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

                System.out.println(receiveMessage(socket));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void sendMessage(DatagramSocket socket, String serverAddress, int serverPort, String message)   {
        try{
        byte[] sendData = message.getBytes();
        InetAddress address = InetAddress.getByName(serverAddress);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, serverPort);
        socket.send(sendPacket);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String receiveMessage(DatagramSocket socket) {
        try {
            byte[] receivedData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
            socket.receive(receivePacket);

            return new String(receivePacket.getData(), 0, receivePacket.getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
