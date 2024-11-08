package org.example.homework2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncFileServer {

    private static final int PORT = 1234;
    private static final String SAVE_DIRECTORY = "received_files/";

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get(SAVE_DIRECTORY));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(executorService);

        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group)
                .bind(new InetSocketAddress(PORT));

        System.out.println("Server started, waiting for connections...");

        acceptClientConnection(serverSocketChannel);

        Thread.sleep(Long.MAX_VALUE);
    }

    private static void acceptClientConnection(AsynchronousServerSocketChannel serverSocketChannel) {
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
                serverSocketChannel.accept(null, this);
                handleClient(clientChannel);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("Failed to accept a connection: " + exc.getMessage());
            }
        });
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(256);

        clientChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                buffer.flip();
                String fileName = new String(buffer.array(), 0, buffer.limit());
                System.out.println("Receiving file: " + fileName);

                Path filePath = Paths.get(SAVE_DIRECTORY, fileName);
                try {
                    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath,
                            StandardOpenOption.CREATE, StandardOpenOption.WRITE);

                    receiveFile(clientChannel, fileChannel, buffer);
                } catch (IOException e) {
                    System.err.println("Error saving file: " + e.getMessage());
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.err.println("Failed to read file name: " + exc.getMessage());
            }
        });
    }

    private static void receiveFile(AsynchronousSocketChannel clientChannel, AsynchronousFileChannel fileChannel, ByteBuffer buffer) {
        clientChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (result == -1) {
                    System.out.println("File received successfully!");
                    closeChannels(fileChannel, clientChannel);
                    return;
                }

                buffer.flip();
                try {
                    writeToFile(fileChannel, buffer);
                } catch (IOException e) {
                    System.err.println("Error writing data to fileChannel: " + e.getMessage());
                    closeChannels(fileChannel, clientChannel);
                }
            }

            private void writeToFile(AsynchronousFileChannel fileChannel, ByteBuffer buffer) throws IOException {
                fileChannel.write(buffer, fileChannel.size(), buffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer buffer) {
                        buffer.clear();
                        clientChannel.read(buffer, buffer, this); // Read the next chunk of file content
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer buffer) {
                        System.err.println("Error writing data to file: " + exc.getMessage());
                    }
                });
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                System.err.println("Failed to receive file content: " + exc.getMessage());
            }
        });
    }

    private static void closeChannels(AsynchronousFileChannel fileChannel, AsynchronousSocketChannel clientChannel) {
        try {
            fileChannel.close();
            clientChannel.close();
        } catch (IOException e) {
            System.err.println("Error closing channels: " + e.getMessage());
        }
    }
}
