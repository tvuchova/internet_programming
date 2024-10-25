import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncFileServer {
    private static final int PORT = 9000;
    private static final int BUFFER_SIZE = 1024;
    private static final String SAVE_DIRECTORY = "uploads/";

    public static void main(String[] args) {
        try {
            // Asynchronous channel group with a fixed thread pool
            AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(
                    4, Executors.defaultThreadFactory());

            // Open server socket channel
            AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel
                    .open(group)
                    .bind(new java.net.InetSocketAddress(PORT));

            System.out.println("Server is listening on port " + PORT);

            // Accept clients asynchronously
            serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                @Override
                public void completed(AsynchronousSocketChannel clientChannel, Void attachment) {
                    // Accept the next connection
                    serverChannel.accept(null, this);
                    handleClient(clientChannel);
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    System.out.println("Failed to accept connection: " + exc.getMessage());
                }
            });

            group.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        clientChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            private FileOutputStream fos;
            private Path filePath = Paths.get(SAVE_DIRECTORY + "uploaded_" + System.currentTimeMillis() + ".dat");

            @Override
            public void completed(Integer bytesRead, ByteBuffer buffer) {
                try {
                    if (bytesRead == -1) {
                        closeConnection();
                        return;
                    }
                    buffer.flip();

                    if (fos == null) {
                        fos = new FileOutputStream(filePath.toFile());
                    }
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    fos.write(data);
                    fos.flush();
                    buffer.clear();

                    clientChannel.read(buffer, buffer, this);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.out.println("Failed to read data from client: " + exc.getMessage());
                closeConnection();
            }

            private void closeConnection() {
                try {
                    if (fos != null) fos.close();
                    clientChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
