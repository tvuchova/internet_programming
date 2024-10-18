package lesson_2.correction_task;

import lesson_2.task1.teacher.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while(!this.serverSocket.isClosed()){
                Socket socket = this.serverSocket.accept();
                System.out.println("A new client has connected!");

                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }

        catch (IOException e){

        }

    }

    public void closeServerSocket(){
        try{
            if(this.serverSocket != null){
                this.serverSocket.close();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
