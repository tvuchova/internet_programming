package org.example.lesson_2.task5_weather.teacher;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class WeatherMulticastServer {
    private static final String MULTICAST_ADDRESS = "224.0.0.1";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (MulticastSocket multicastSocket = new MulticastSocket()) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            System.out.println("Server is started:");

            while (true) {
                String city = System.console().readLine("Въведете име на град (или 'exit' за изход): ");
                if ("exit".equalsIgnoreCase(city.trim())) {
                    break;
                }

                String weatherForecast = generateWeatherForecast(city);
                byte[] buffer = weatherForecast.getBytes();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                multicastSocket.send(packet);
                System.out.println("Изпратена прогноза за " + city + ": " + weatherForecast);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String generateWeatherForecast(String city) {
        return "Forecast for  " + city + ": Sunny, 25°C.";
    }
}
