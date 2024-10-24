package org.example.lesson_2.zadacha6.student;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTransferServer {
    private static final int PORT = 9090;
    private static final int BUFFER_SIZE = 4096; // Should be edited depending on the file size
    private static final int ThreadPoolSize = 4;

    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(ThreadPoolSize);
        AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open().bind(new java.net.InetSocketAddress(PORT));
        System.out.println("Server listening on port " + PORT);

        serverChannel.accept(null, new java.nio.channels.CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
                serverChannel.accept(null, this);
                threadPool.submit(() -> handleClient(clientChannel));
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("Failed to accept connection: " + exc.getMessage());
            }
        });

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        clientChannel.read(buffer, buffer, new java.nio.channels.CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer bytesRead, ByteBuffer buf) {
                if (bytesRead > 0) {
                    buf.flip();
                    byte[] data = new byte[buf.remaining()];
                    buf.get(data);
                    String fileName = new String(data);
                    System.out.println("Receiving file: " + fileName);
                    saveFile(clientChannel, fileName);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buf) {
                System.out.println("Failed to read file name: " + exc.getMessage());
                try {
                    clientChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void saveFile(AsynchronousSocketChannel clientChannel, String fileName) {
        Path path = Paths.get("ReceivedFiles", fileName);
        try {
            Files.createDirectories(path.getParent());
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            clientChannel.read(buffer, buffer, new java.nio.channels.CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer bytesRead, ByteBuffer buf) {
                    if (bytesRead > 0) {
                        buf.flip();
                        try {
                            fileChannel.write(buf, fileChannel.size());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("File received successfully: " + fileName);
                        try {
                            fileChannel.close();
                            clientChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer buf) {
                    System.out.println("Failed to read file data: " + exc.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}