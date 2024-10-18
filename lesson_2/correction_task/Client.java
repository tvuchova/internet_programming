package lesson_2.correction_task;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.username = username;
        }

        catch (IOException e){
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            this.bufferedWriter.write(this.username);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while(this.socket.isConnected()){
                String message = scanner.nextLine();
                this.bufferedWriter.write(this.username + ": " + message);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
            }
        }

        catch (IOException e){
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void listen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                while(socket.isConnected()){
                    try{
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    }

                    catch(IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }

            if(bufferedWriter != null){
                bufferedWriter.close();
            }

            if(socket != null){
                socket.close();
            }
        }

        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a username to connect to the chat: ");
        String username = scanner.nextLine();

        Socket socket = new Socket("localhost", 7777);
        Client client = new Client(socket, username);

        client.listen();
        client.sendMessage();
    }
}
