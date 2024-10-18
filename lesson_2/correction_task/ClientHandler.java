package lesson_2.correction_task;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = this.bufferedReader.readLine();

            if (clientExists(this.clientUsername)) {
                this.bufferedWriter.write("SERVER: Username '" + this.clientUsername + "' is already taken!");
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
                this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
                return;
            }
            
            clientHandlers.add(this);

            this.broadcastMessage("SERVER: " + this.clientUsername + " has entered the chat!");
        }

        catch (IOException e){
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    private boolean clientExists(String clientUsername){
        for(ClientHandler clientHandler : clientHandlers){
            if(clientHandler.getUsername().equals(clientUsername)){
                return true;
            }
        }

        return false;
    }

    @Override
    public void run() {
        String clientMessage;

        while(this.socket.isConnected()){
            try{
                clientMessage = bufferedReader.readLine();
                this.broadcastMessage(clientMessage);
            }

            catch (IOException e){
                this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String message){
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(this.clientUsername)){
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }

            catch (IOException e){
                this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
            }
        }
    }

    private void removeClientHandler(){
        clientHandlers.remove(this);
        this.broadcastMessage("SERVER: " + this.clientUsername + "has left the chat!");
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.removeClientHandler();

        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername () {
        return this.clientUsername;
    }
}
