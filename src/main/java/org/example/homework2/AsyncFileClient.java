package org.example.homework2;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class AsyncFileClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws Exception {
        String filePathInput = getFilePathFromUser();

        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        socketChannel.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                System.out.println("Connected to server, starting file transfer...");
                sendFile(socketChannel, filePathInput);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.err.println("Failed to connect to server: " + exc.getMessage());
                closeChannel(socketChannel);
            }
        });

        // Keep the main thread alive
        Thread.sleep(Long.MAX_VALUE);
    }

    private static String getFilePathFromUser() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the file path to send: ");
            return scanner.nextLine();
        }
    }

    private static void sendFile(AsynchronousSocketChannel socketChannel, String filePathInput) {
        Path filePath = Paths.get(filePathInput);
        File file = filePath.toFile();

        if (!file.exists() || !file.isFile()) {
            System.err.println("File does not exist: " + filePathInput);
            return;
        }

        sendFileName(socketChannel, file);
    }

    private static void sendFileName(AsynchronousSocketChannel socketChannel, File file) {
        ByteBuffer buffer = ByteBuffer.wrap(file.getName().getBytes());
        socketChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                System.out.println("Sent file name: " + file.getName());
                sendFileContent(socketChannel, file);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.err.println("Failed to send file name: " + exc.getMessage());
                closeChannel(socketChannel);
            }
        });
    }

    private static void sendFileContent(AsynchronousSocketChannel socketChannel, File file) {
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file.toPath(), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            while (true) {
                int bytesRead = fileChannel.read(buffer, position).get();

                if (bytesRead == -1) {
                    System.out.println("File sent successfully!");
                    break;
                }

                buffer.flip();
                socketChannel.write(buffer).get();

                buffer.clear();
                position += bytesRead;
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("Error sending file content: " + e.getMessage());
        }


    }

    private static void closeChannel(AsynchronousSocketChannel socketChannel) {
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
                System.out.println("Socket channel closed.");
            }
        } catch (IOException e) {
            System.err.println("Error closing socket channel: " + e.getMessage());
        }
    }
}
