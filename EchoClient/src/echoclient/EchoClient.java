package echoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class EchoClient implements Runnable{

    public EchoClient()
    {
       
    }
    
    @Override
    public void run() {
         try {
            System.out.println("Client: Waiting for connection.....");
            InetAddress localAddress = InetAddress.getLocalHost();
            Socket clientSocket = new Socket(localAddress, 6600);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //<editor-fold desc="Ask for user input">
            System.out.println("Client: Connected to server");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter text: ");
                String inputLine = scanner.nextLine();
                if ("quit".equalsIgnoreCase(inputLine)) {
                    break;
                }
                out.println(inputLine);
                String response = br.readLine();
                System.out.println("Server response: " + response);
            }
            //</editor-fold>
            
            
        } catch (IOException ex) {
            System.out.println("Could not connect!");
            ex.printStackTrace();
            ArrayList<String> g;
        }
    }

}
