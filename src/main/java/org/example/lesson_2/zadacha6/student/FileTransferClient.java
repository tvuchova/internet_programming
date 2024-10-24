package org.example.lesson_2.zadacha6.student;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.channels.CompletionHandler;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class FileTransferClient {
    private static final int PORT = 9090;
    private static final String HOST = "localhost";
    private static final int BUFFER_SIZE = 4096;

    private static final int ThreadPoolSize = 4;

    public static void main(String[] args) throws IOException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(ThreadPoolSize));
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open(group);
        clientChannel.connect(new java.net.InetSocketAddress(HOST, PORT), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void result, Void attachment) {
                System.out.println("Connected to server.");
                String filePath = new Scanner(System.in).nextLine();
                sendFile(clientChannel, filePath);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                System.out.println("Failed to connect to server: " + exc.getMessage());
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

    private static void sendFile(AsynchronousSocketChannel clientChannel, String filePath) {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            clientChannel.write(ByteBuffer.wrap(path.getFileName().toString().getBytes()), null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    readFileAndSend(clientChannel, path, buffer);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.out.println("Failed to send file name: " + exc.getMessage());
                }
            });
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    private static void readFileAndSend(AsynchronousSocketChannel clientChannel, Path path, ByteBuffer buffer) {
        try {
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path);
            fileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer bytesRead, ByteBuffer buf) {
                    if (bytesRead > 0) {
                        buf.flip();
                        clientChannel.write(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer buf) {
                                buf.clear();
                                readFileAndSend(clientChannel, path, buf);
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer buf) {
                                System.out.println("Failed to send file data: " + exc.getMessage());
                            }
                        });
                    } else {
                        System.out.println("File sent successfully.");
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
                    System.out.println("Failed to read file: " + exc.getMessage());
                }
            });
        } catch (IOException e) {
            System.out.println("Error opening file for sending: " + e.getMessage());
        }
    }
}

