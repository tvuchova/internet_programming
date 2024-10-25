import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncFileServer {
    private static final Logger logger = Logger.getLogger(AsyncFileServer.class.getName());
    private final AsynchronousServerSocketChannel server;

    public static void main(String[] args) throws IOException {
        AsyncFileServer server = new AsyncFileServer("localhost", 6969);
        server.start();
    }

    public AsyncFileServer(String address, int port) throws IOException {
        AsynchronousChannelGroup group = AsynchronousChannelGroup.withFixedThreadPool(10, Executors.defaultThreadFactory());
        server = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(address, port));
        System.out.println("Server running at " + address + ":" + port);
    }

    public void start() {
        while (true) {
            try {
                Future<AsynchronousSocketChannel> future = server.accept();
                AsynchronousSocketChannel client = future.get();
                handleClient(client);
            } catch (IOException | InterruptedException | ExecutionException e) {
                logger.log(Level.SEVERE, "Server encountered an error", e);
            }
        }
    }

    private void handleClient(AsynchronousSocketChannel client) throws IOException, InterruptedException, ExecutionException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        client.read(buffer).get();
        buffer.flip();
        String fileName = new String(buffer.array(), 0, buffer.limit()).trim();
        buffer.clear();

        Path dirPath = Paths.get("files");
        if (!Files.exists(dirPath)) {
            Files.createDirectory(dirPath);
        }

        Path filePath = dirPath.resolve(fileName);
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            long position = 0;
            while (client.read(buffer).get() != -1) {
                buffer.flip();
                fileChannel.write(buffer, position).get();
                position += buffer.remaining();
                buffer.clear();
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "File handling error", e);
        } finally {
            try {
                if (client.isOpen()) client.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing client channel", e);
            }
        }

        System.out.println("Received file: " + fileName);
    }
}