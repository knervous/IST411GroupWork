/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Brian
 */
public class HTTPClient implements Runnable{
    
    public HTTPClient(){
        
    }
    
    
    @Override
    public void run(){
        System.out.println("CLIENT: HTTP Client Started");
        try {
            InetAddress serverInetAddress = InetAddress.getByName("127.0.0.1");
            Socket connection = new Socket(serverInetAddress, 80);
            
            try (
                    OutputStream out = connection.getOutputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                )
                {
                sendGet(out);
                System.out.println(getResponse(in));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } catch (Exception e) {
        }
        
    }
    /**
     * Sends GET to the server using a string of bytes written to the 
     * OutputStream class
     * @param out OutputStream object that will write to the socket
     */
    private void sendGet(OutputStream out){
        try {
            out.write("GET /default\r\n".getBytes());
            out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Receives a BufferedReader object (in)
     * <p>
     * 
     * @param in
     * @return 
     */
    private String getResponse (BufferedReader in){
        try {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {                
                response.append(inputLine).append("\n");
                
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

