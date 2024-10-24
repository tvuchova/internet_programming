package com.valentin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncFileServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 4096;
    private static final int MAX_CONNECTIONS = 10;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CONNECTIONS);
        try {
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(threadPool);
            AsynchronousServerSocketChannel serverSocket = AsynchronousServerSocketChannel.open(channelGroup)
                    .bind(new java.net.InetSocketAddress(PORT));
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Future<AsynchronousSocketChannel> future = serverSocket.accept();
                handleClient(future.get());
            }
        } catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            String outputFile = "received_" + System.currentTimeMillis() + ".dat";
            FileOutputStream fos = new FileOutputStream(outputFile);
            System.out.println("Receiving file: " + outputFile);

            while (clientChannel.read(buffer).get() != -1) {
                buffer.flip();
                fos.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }

            fos.close();
            System.out.println("File received successfully.");
            clientChannel.close();
        } catch (IOException | InterruptedException | java.util.concurrent.ExecutionException e) {
            e.printStackTrace();
        }
    }
}
