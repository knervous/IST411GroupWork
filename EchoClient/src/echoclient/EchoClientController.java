/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient;

/**
 *
 * @author Paul
 */
public class EchoClientController {

    public static void main(String args[]) {

        EchoServer server = new EchoServer();
        EchoClient client = new EchoClient();

        Thread serverThread = new Thread(server);
        Thread clientThread = new Thread(client);
        
        serverThread.start();
        while (server.getServerSocket() == null) {
            clientThread.start();
            break;
        }

    }

}
