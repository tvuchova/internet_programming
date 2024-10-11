package lesson_2.zadacha1.student;

import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class TCPServer {
    public static void main(String[] args) {
        try {

            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Сървърът е стартиран и слуша на порт 1234...");


            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                System.out.println("Интерфейс: " + networkInterface.getDisplayName());
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    System.out.println("Адрес: " + address.getHostAddress());
                }
            }


            while (true) {

                Socket clientSocket = serverSocket.accept();


                InetAddress clientAddress = clientSocket.getInetAddress();
                System.out.println("Нов клиент се свърза: " + clientAddress.getHostAddress());


                handleClient(clientSocket);


                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Грешка при стартиране на сървъра: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);


        String request = in.readLine();
        System.out.println("Заявка от клиента: " + request);


        if (request.startsWith("ADD")) {

            out.println("Студентът е добавен.");
        } else if (request.equals("LIST")) {

            out.println("Списъкът с оценки.");
        } else if (request.equals("AVERAGE")) {

            out.println("Средната оценка е 4.5.");
        } else {
            out.println("Невалидна команда.");
        }


        in.close();
        out.close();
    }
}