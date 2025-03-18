package networking;
import java.io.*;
import java.net.*;

// Networking: Server
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new ClientHandler(clientSocket).start();
        }
    }
}

// ClientHandler for Multithreading
class ClientHandler extends Thread {
    private Socket clientSocket;
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String input;
            while ((input = in.readLine()) != null) {
                out.println("Server received: " + input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}