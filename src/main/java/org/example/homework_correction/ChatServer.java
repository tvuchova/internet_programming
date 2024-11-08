package org.example.homework_correction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Map<String, ClientHandler> users = new ConcurrentHashMap<>();
    private static ExecutorService executorService;

    public static void main(String[] args) {
        System.out.println("Chat Registration Server is running...");

        executorService = Executors.newVirtualThreadPerTaskExecutor();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }

    public static void broadcastMessage(String message, ClientHandler sender) {
        users.values().forEach(client -> {
            if (client != sender) {
                client.sendMessage(message);
            }
        });
    }

    public static boolean registerUser(String username, ClientHandler clientHandler) {
        if (users.putIfAbsent(username, clientHandler) == null) {
            return true;
        }
        return false;
    }

    public static void removeUser(String username) {
        users.remove(username);
        System.out.println(username + " has disconnected.");
        broadcastMessage(username + " has left the chat.", null);
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                out.println("Enter a unique username: ");
                username = in.readLine();

                if (username == null || username.trim().isEmpty()) {
                    out.println("Username cannot be empty.");
                    continue;
                }

                if (ChatServer.registerUser(username, this)) {
                    out.println("Welcome to the chat, " + username + "!");
                    ChatServer.broadcastMessage(username + " has joined the chat.", this);
                    break;
                } else {
                    out.println("Username is already taken. Try another one.");
                }
            }

            String message;
            while ((message = in.readLine()) != null) {
                if ("exit".equalsIgnoreCase(message)) {
                    ChatServer.removeUser(username);
                    break;
                }
                ChatServer.broadcastMessage(username + ": " + message, this);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeResources();
            if (username != null) {
                ChatServer.removeUser(username);
            }
        }
    }

    private void closeResources() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}
