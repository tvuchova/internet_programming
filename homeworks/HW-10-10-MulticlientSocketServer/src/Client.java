import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Enter a command (ADD, VIEW, AVERAGE): ");
            String command = userInput.readLine().toUpperCase();
            out.println(command);

            switch (command) {
                case "ADD":
                    System.out.println("Enter student data (e.g. Name,Grade1,Grade2,...): ");
                    String studentData = userInput.readLine();
                    out.println(studentData);
                    break;
                case "VIEW":
                    break;
                case "AVERAGE":
                    System.out.println("Enter student name: ");
                    String studentName = userInput.readLine();
                    out.println(studentName);
                    break;
                default:
                    System.out.println("Invalid command.");
                    return;
            }

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
