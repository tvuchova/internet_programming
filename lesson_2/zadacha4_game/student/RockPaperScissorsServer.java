package lesson_2.zadacha4_game.student;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RockPaperScissorsServer {
    public static void main(String[] args) {
        try{
            DatagramSocket serverSocket = new DatagramSocket(12345);
            byte[] receivedData = new byte[1024];
            System.out.println("Server is running...");
            while (true) {
                DatagramPacket receivedSocket = new DatagramPacket(receivedData, receivedData.length);
                serverSocket.receive(receivedSocket);

                String clientChoice = new String(receivedSocket.getData(), 0, receivedSocket.getLength());
                InetAddress clientAddress = receivedSocket.getAddress();
                int clientPort = receivedSocket.getPort();

                System.out.println("Client choice is: " + clientChoice);
                String result = getResult(clientChoice);

                byte[] sendData = result.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
