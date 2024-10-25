package org.example.lesson_2.task5_weather.teacher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class WeatherMulticastClient {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (MulticastSocket multicastSocket = new MulticastSocket(PORT)) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            multicastSocket.joinGroup(group);
            System.out.println("Client join the group.Wait to receive forecast...");

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received message: " + message);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}