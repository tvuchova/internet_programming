import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncFileClient {

    private static final Logger logger = Logger.getLogger(AsyncFileClient.class.getName());
    private final AsynchronousSocketChannel socketChannel;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the file path to upload:");
        String filePath = scanner.nextLine();

        AsyncFileClient client = new AsyncFileClient("localhost", 6969);
        client.uploadFile(filePath);
    }

    public AsyncFileClient(String host, int port) throws IOException {
        AsynchronousSocketChannel tempChannel = null;
        try {
            tempChannel = AsynchronousSocketChannel.open();
            Future<Void> future = tempChannel.connect(new InetSocketAddress(host, port));
            future.get();
            System.out.println("Connected to server at " + host + ":" + port);
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Connection error", e);
        }
        socketChannel = tempChannel;
    }

    public void uploadFile(String filePath) {
        Path path = Paths.get(filePath);
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            String fileName = path.getFileName().toString();
            System.out.println("Uploading file: " + fileName);
            ByteBuffer buffer = ByteBuffer.wrap(fileName.getBytes());
            socketChannel.write(buffer).get();
            buffer.clear();

            ByteBuffer fileBuffer = ByteBuffer.allocate(1024);
            long position = 0;
            while (true) {
                Future<Integer> readResult = fileChannel.read(fileBuffer, position);
                int bytesRead = readResult.get();
                if (bytesRead == -1) break;

                fileBuffer.flip();
                socketChannel.write(fileBuffer).get();
                position += bytesRead;
                fileBuffer.clear();
            }

            System.out.println("File " + fileName + " uploaded to server.");
        } catch (IOException | InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "File upload error", e);
        } finally {
            try {
                if (socketChannel.isOpen()) socketChannel.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing socket channel", e);
            }
        }
    }
}