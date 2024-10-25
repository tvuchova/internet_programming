package org.example.lesson_2.task4_game.teacher;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RockPaperScissorsServer {
    public static void main(String[] args) {
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Server is started on port " + port + "...");

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String clientChoice = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Получен избор от клиента: " + clientChoice);

                String serverResponse = getResult(clientChoice);
                byte[] responseBuffer = serverResponse.getBytes();

                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length,
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            System.err.print("Error:" + e.getMessage());
        }
    }

    private static String getResult(String clientChoice) {
        String[] choices = {"Rock", "Scissors", "Paper"};
        String serverChoice = choices[(int) (Math.random() * 3)];
        System.out.println("Server choice is: " + serverChoice);

        if (clientChoice.equals(serverChoice)) {
            return "Draw! Both of you chose " + clientChoice + ".";
        } else if (("Rock".equalsIgnoreCase(clientChoice) && "Scissors".equalsIgnoreCase(serverChoice)) ||
                ("Scissors".equalsIgnoreCase(clientChoice) && "Paper".equalsIgnoreCase(serverChoice)) ||
                ("Paper".equalsIgnoreCase(clientChoice) && "Rock".equalsIgnoreCase(serverChoice))) {
            return "You win! You chose " + clientChoice + ", аnd server chose " + serverChoice + ".";
        } else {
            return "You lose! You chose " + clientChoice + ", аnd server chose " + serverChoice + ".";
        }
    }
}
