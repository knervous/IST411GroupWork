/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpapp;

/**
 *
 * @author Brian
 */
public class HTTPApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WebServer server = new WebServer();
        HTTPClient client = new HTTPClient();
        
        Thread serverThread = new Thread(server);
        Thread clientThread = new Thread(client);
        
        serverThread.start();
        clientThread.start();
//        while (server.getServerSocket() == null){
//            clientThread.start();
//            break;
        }
    
        
    }
   
