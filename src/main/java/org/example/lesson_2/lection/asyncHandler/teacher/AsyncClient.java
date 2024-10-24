package org.example.lesson_2.lection.asyncHandler.teacher;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AsyncClient {
    public static void main(String[] args) throws Exception {
        try (AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open()) {
            InetSocketAddress hostAddress = new InetSocketAddress("127.0.0.1", 8090);
            // Connect to the server
            socketChannel.connect(hostAddress, null, new CompletionHandler<Void, Object>() {
                @Override
                public void completed(Void result, Object attachment) {
                    // Send a message to the server
                    String message = "Hi Server! I am an Asynchronous Client.";
                    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());

                    // Asynchronously send the message to the server
                    socketChannel.write(buffer, null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            System.out.println("Message sent to server.");

                            // Optionally, read the server's response
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            socketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                @Override
                                public void completed(Integer bytesRead, ByteBuffer readBuffer) {
                                    if (bytesRead != -1) {
                                        readBuffer.flip();
                                        String response = new String(readBuffer.array(), 0, readBuffer.limit());
                                        System.out.println("Received response from server: " + response);
                                        readBuffer.clear();
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, ByteBuffer attachment) {
                                    if (exc instanceof java.nio.channels.AsynchronousCloseException) {
                                        System.err.println("Connection closed unexpectedly while reading: " + exc.getMessage());
                                    } else {
                                        System.err.println("Failed to read response from server: " + exc.getMessage());
                                    }
                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            System.err.println("Message not sent to server: " + exc.getMessage());
                        }
                    });
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.err.println("Failed to connect to server: " + exc.getMessage());
                }
            });

            // Prevent client from exiting prematurely
            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("Failed to open client socket channel: " + e.getMessage());
        }
    }
}
