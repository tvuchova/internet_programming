package sockets_part3.finalized.simple_client_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFinal {
    public static void main(String[] args) {
        System.out.println("Hello World.I am Server!");

        try {
            ServerSocket ss =new ServerSocket(9708);
            Socket soc = ss.accept();
            System.out.println("Connection established");
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String str = in.readLine();
            PrintWriter out = new PrintWriter(soc.getOutputStream(),true);
            out.println("Server echo says: " + str);
            ss.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
