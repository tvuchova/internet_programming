package org.example.lesson_2.lection.asyncHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

public class AsyncServerWithGroup {

    public static void main(String[] args) throws Exception {
        // Create a channel group with a fixed thread pool
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(10, Executors.defaultThreadFactory());
        // Create the server socket channel
        try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open(group)) {
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
            serverSocketChannel.bind(hostAddress);

            System.out.println("Server listening on port 8090...");

            // Accept the first client connection
            serverSocketChannel.accept(null, new CompletionHandler<>() {
                @Override
                public void completed(AsynchronousSocketChannel clientChannel, Object attachment) {
                    // Accept the next client connection
                    serverSocketChannel.accept(null, this);

                    // Allocate a buffer to read data from the client
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    // Read data from the client
                    handleClient(clientChannel, buffer);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.err.println("Failed to accept a connection: " + exc.getMessage());
                }
            });

            // Keep the main thread alive to listen for connections
            Thread.sleep(Long.MAX_VALUE);
        } catch (IOException e) {
            System.err.println("Error creating server: " + e.getMessage());
        }
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel, ByteBuffer buffer) {
        clientChannel.read(buffer, buffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer bytesRead, ByteBuffer buffer) {
                if (bytesRead == -1) {
                    try {
                        clientChannel.close(); // Close channel if client disconnects
                    } catch (Exception e) {
                        System.out.println("Error closing channel: " + e.getMessage());
                    }
                    return;
                }

                buffer.flip();
                String message = new String(buffer.array(), 0, buffer.limit());
                System.out.println("Received message from client: " + message);
                buffer.clear();

                // Echo the message back to the client
                ByteBuffer responseBuffer = ByteBuffer.wrap(("Server received: " + message).getBytes());
                handleServerResponse(responseBuffer);
            }

            private void handleServerResponse(ByteBuffer responseBuffer) {
                clientChannel.write(responseBuffer, responseBuffer, new CompletionHandler<>() {
                    @Override
                    public void completed(Integer result, ByteBuffer responseBuffer) {
                        if (responseBuffer.hasRemaining()) {
                            clientChannel.write(responseBuffer, responseBuffer, this); // Continue writing if not done
                        } else {
                            System.out.println("Response sent to client.");
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer responseBuffer) {
                        System.err.println("Failed to send response to client: " + exc.getMessage());
                    }
                });
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                System.err.println("Failed to read from client: " + exc.getMessage());
            }
        });
    }
}

