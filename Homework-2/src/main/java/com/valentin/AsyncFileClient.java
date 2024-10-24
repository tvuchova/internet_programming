package com.valentin;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Future;

public class AsyncFileClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java AsyncFileClient <file-path>");
            return;
        }

        String filePath = args[0];
        Path path = Paths.get(filePath);

        try (AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open()) {
            Future<Void> connectFuture = clientChannel.connect(new java.net.InetSocketAddress(SERVER_HOST, SERVER_PORT));
            connectFuture.get();
            System.out.println("Connected to server.");

            byte[] fileBytes = Files.readAllBytes(path);
            ByteBuffer buffer = ByteBuffer.wrap(fileBytes);

            Future<Integer> writeResult = clientChannel.write(buffer);
            writeResult.get();
            System.out.println("File sent successfully.");

            clientChannel.close();
        } catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        }
    }
}
