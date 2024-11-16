package org.lecture.http.clients;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

@Slf4j
public class GetMethodSocketExample {

    private static final String HOST = "example.com";
    private static final int PORT = 80;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            out.println("GET / HTTP/1.1");
            out.println("Host: " + HOST);
            out.println("Connection: close");
            out.println();

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                log.info(responseLine);
            }
        } catch (UnknownHostException e) {
            log.error("Error: Unknown host - " + HOST);
        } catch (IOException e) {
            log.error("Error: Unable to establish connection to " + HOST);
        }
    }
}
