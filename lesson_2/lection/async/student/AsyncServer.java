package lesson_2.lection.async.student;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Future;

public class AsyncServer {
    public static void main(String[] args) throws Exception {
        //creates a new asynchronous server socket channel
        //server listen for client connections
        //accepts a new connection asynchronously
        //wait for client to connect,block till the client connects
        //buffer to store data
        //block till data is read
        //flip the buffer to start reading
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 4555));
        Future<AsynchronousSocketChannel> accept = serverSocketChannel.accept();
        AsynchronousSocketChannel socketChannel = accept.get();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Future<Integer> read = socketChannel.read(buffer);
        read.get();

        buffer.flip();
        System.out.println(new String(buffer.array()).trim());


    }
}


