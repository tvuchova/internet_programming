package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

public class AsyncFileClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 9000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        String filePath = "path/to/local/file.dat";

        try {
            AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open();
            clientChannel.connect(new java.net.InetSocketAddress(SERVER_ADDRESS, SERVER_PORT)).get();

            System.out.println("Connected to server. Sending file...");

            Path path = Paths.get(filePath);
            byte[] fileData = Files.readAllBytes(path);

            ByteBuffer buffer = ByteBuffer.wrap(fileData);
            clientChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer bytesWritten, ByteBuffer buffer) {
                    if (buffer.hasRemaining()) {
                        clientChannel.write(buffer, buffer, this);
                    }

                    else {
                        System.out.println("File sent successfully.");
                        closeChannel();
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer buffer) {
                    System.out.println("Failed to send file: " + exc.getMessage());
                    closeChannel();
                }

                private void closeChannel() {
                    try {
                        clientChannel.close();
                    }

                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread.sleep(2000);

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
