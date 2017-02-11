package echoclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer implements Runnable {

    private ServerSocket serverSocket;

    public EchoServer() {
      
    }

    @Override
    public void run() {
        try (ServerSocket tempSocket = new ServerSocket(6600)) {
            serverSocket = tempSocket;
            System.out.println("Server: Waiting for client connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Server: Connected to client");

            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                String newline = "";
                for (char letter : inputLine.toCharArray()) {
                    newline += Character.toUpperCase(letter);
                }
                //newline = inputLine;
                System.out.println("Server: " + newline);
                out.println(newline);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

}
