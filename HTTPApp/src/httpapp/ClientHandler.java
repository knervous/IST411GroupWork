/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Receives a Socket object and sends it to handleRequest method. 
 * This thread is runnable, which allows it to run as a concurrent thread.
 * 
 * @author Brian
 */
public class ClientHandler implements Runnable{
    
    private final Socket socket; //new Socket object named socket.
    
    public ClientHandler(Socket socket){
        this.socket = socket;
        
    }
    
    /**
     * Notifies console it has a socket connection.
     * <p>
     * Passes the socket connection to handleRequest()
     */
    @Override
    public void run(){
        System.out.println("\nSERVER: ClientHandler Started for " + 
                this.socket);
        handleRequest(this.socket);
        System.out.println("SERVER: ClientHandler Terminated for " +
                this.socket + "\n");
    }

    /**
     * Creates the buffered reader and input stream and receives it by calling
     * the socket.getInputStream method.  
     * <p>
     * Creates a string to read the first line.
     * 
     * Then it tokenizes the headerline and parses it into the httpMethod string.
     * 
     * @param socket A socket object passed in from run()
     */
    private void handleRequest(Socket socket) {
        try (BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));){ //input stream created from socket
            
            String headerLine = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(headerLine); //constructor
            String httpMethod = tokenizer.nextToken();
            
            if (httpMethod.equals("GET")) {
                System.out.println("Get method processed");
                String httpQueryString = tokenizer.nextToken();
                StringBuilder responseBuffer = new StringBuilder();
                responseBuffer
                        .append("<html><h1>WebServer Home Page....</h1><br>")
                        .append("<b>Welcome to my web server!</b><BR>")
                        .append("</html>");
                sendResponse(socket, 200, responseBuffer.toString());
            }
            else{
                System.out.println("The HTTP method is not recognized");
                sendResponse(socket, 405, "Method Not Allowed");
            }
            /*
            Additional lines could be processed here by using:
            String httpQueryString = tokenizer.nextToken();
            This allows you to read more from the line.
            Would like want to move GET into a method so the entire length 
            would be processed
            */
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * Method to format the server's response in HTML and send it to the client.
 * 
 * @param socket Socket object for the connection to respond to
 * @param statusCode Status code to be sent to the client
 * @param responseString Response string that will be sent
 */
    private void sendResponse(Socket socket, int statusCode, String responseString) {
        String statusLine;
        String serverHeader = "Server: WebServer\r\n";
        String contentTypeHeader = "Content-Type: text/html\r\n";
        try (
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                ){
            if(statusCode == 200){
                statusLine = "HTTP/1.0 200 OK" + "\r\n";
                String contentLengthHeader = "Content-Length: " +
                        responseString.length() + "\r\n";
                out.writeBytes(statusLine);             //status line
                out.writeBytes(serverHeader);           //header
                out.writeBytes(contentTypeHeader);      //header
                out.writeBytes(contentLengthHeader);    //header
                out.writeBytes("\r\n");                 //blank line
                out.writeBytes(responseString);         //message body
            }
            else if (statusCode == 405) {
                statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
                out.writeBytes(statusLine);             //status line
                out.writeBytes("\r\n");                 //blank line
            }
            else {
                statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
                out.writeBytes(statusLine);             //status line
                out.writeBytes("\r\n");                 //blank line

            }
            out.close();
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
