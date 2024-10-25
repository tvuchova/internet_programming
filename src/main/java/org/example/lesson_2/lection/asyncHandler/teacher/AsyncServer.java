package org.example.lesson_2.lection.asyncHandler.teacher;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncServer {
    public static void main(String[] args) throws Exception {
        // Use try-with-resources to ensure the server socket channel is closed
        try (AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open()) {
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
            serverSocketChannel.bind(hostAddress);

            System.out.println("Server listening on port 8090...");

            // Accept the first client connection asynchronously
            serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
                    // Accept the next client connection
                    serverSocketChannel.accept(null, this);

                    // Handle the client connection in a separate method
                    handleClient(clientChannel);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.err.println("Failed to accept a connection: " + exc.getMessage());
                }
            });

            // Keep the server running indefinitely
            Thread.sleep(Long.MAX_VALUE);
        } catch (IOException e) {
            System.err.println("Server socket channel failed: " + e.getMessage());
        }
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        clientChannel.read(buffer, buffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer bytesRead, ByteBuffer buffer) {
                if (bytesRead == -1) {
                    // Client has disconnected
                    closeClientChannel(clientChannel);
                    return;
                }

                // Process the message from the client
                buffer.flip();
                String message = new String(buffer.array(), 0, buffer.limit());
                System.out.println("Received message from client: " + message);
                buffer.clear();

                // Echo the message back to the client
                ByteBuffer responseBuffer = ByteBuffer.wrap(("Server received: " + message).getBytes());
                sendResponse(clientChannel, responseBuffer);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer buffer) {
                System.err.println("Failed to read from client: " + exc.getMessage());
                closeClientChannel(clientChannel);
            }
        });
    }

    private static void sendResponse(AsynchronousSocketChannel clientChannel, ByteBuffer responseBuffer) {
        clientChannel.write(responseBuffer, responseBuffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, ByteBuffer responseBuffer) {
                if (responseBuffer.hasRemaining()) {
                    // Continue writing if there's still data to send
                    clientChannel.write(responseBuffer, responseBuffer, this);
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

    private static void closeClientChannel(AsynchronousSocketChannel clientChannel) {
        try {
            clientChannel.close();
            System.out.println("Client connection closed.");
        } catch (IOException e) {
            System.err.println("Failed to close client channel: " + e.getMessage());
        }
    }
}


