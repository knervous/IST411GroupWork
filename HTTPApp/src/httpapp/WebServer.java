package httpapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The WebServer class creates a ServerSocket object on port 80. 
 * The remote object is a Socket object that is created once accept has a connection. 
 * Once the connection is made, a new thread is created.
 * The thread is passed a runnable object, which also receives the remote Socket obj
 * 
 * @author Brian
 */
public class WebServer implements Runnable {
    
    private ServerSocket serverSocket;
    
    public WebServer() {

    }
    
    @Override
    public void run(){
        System.out.println("SERVER: Webserver Started");
        try (ServerSocket tempSocket = new ServerSocket(80)){ //try with resources block
            serverSocket = tempSocket;
            while (true){
                System.out.println("SERVER: Waiting for client request" + Thread.currentThread());
                Socket remote = serverSocket.accept(); //waits for request
                System.out.println("SERVER: Connection made from " + remote.toString());
                new Thread(new ClientHandler(remote)).start(); //create a new handler thread and start it
            }
        } 
        
        catch (IOException ex) {
            ex.printStackTrace();
        }        
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
