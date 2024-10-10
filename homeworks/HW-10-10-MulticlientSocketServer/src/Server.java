import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server started...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request = in.readLine();
                System.out.println("Request received: " + request);

                switch (request) {
                    case "ADD":
                        String studentData = in.readLine();
                        addStudent(studentData);
                        out.println("Student added successfully!");
                        break;
                    case "VIEW":
                        List<String> students = viewStudents();
                        for (String student : students) {
                            out.println(student);
                        }
                        break;
                    case "AVERAGE":
                        String studentName = in.readLine();
                        double average = calculateAverage(studentName);
                        if (average >= 0) {
                            out.println("Average for " + studentName + ": " + average);
                        } else {
                            out.println("Student not found.");
                        }
                        break;
                    default:
                        out.println("Invalid request.");
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private synchronized void addStudent(String studentData) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(studentData);
                writer.newLine();
            }
        }

        private synchronized List<String> viewStudents() throws IOException {
            List<String> students = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    students.add(line);
                }
            }
            return students;
        }

        private synchronized double calculateAverage(String studentName) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[0].equalsIgnoreCase(studentName)) {
                        int total = 0;
                        int count = 0;
                        for (int i = 1; i < data.length; i++) {
                            total += Integer.parseInt(data[i]);
                            count++;
                        }
                        return total / (double) count;
                    }
                }
            }
            return -1;
        }
    }
}
