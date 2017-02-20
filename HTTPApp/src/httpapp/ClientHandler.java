/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Receives a Socket object and sends it to handleRequest method. This thread is
 * runnable, which allows it to run as a concurrent thread.
 *
 * @author Brian
 */
public class ClientHandler implements Runnable {

    private final Socket socket; //new Socket object named socket.

    public ClientHandler(Socket socket) {
        this.socket = socket;

    }

    /**
     * Notifies console it has a socket connection.
     * <p>
     * Passes the socket connection to handleRequest()
     */
    @Override
    public void run() {
        System.out.println("\nSERVER: ClientHandler Started for "
                + this.socket);
        handleRequest(this.socket);
        System.out.println("SERVER: ClientHandler Terminated for "
                + this.socket + "\n");
    }

    /**
     * Creates the buffered reader and input stream and receives it by calling
     * the socket.getInputStream method.
     * <p>
     * Creates a string to read the first line.
     *
     * Then it tokenizes the headerline and parses it into the httpMethod
     * string.
     *
     * @param socket A socket object passed in from run()
     */
    private void handleRequest(Socket socket) {
        StringBuilder diaryText = new StringBuilder();
        try {
            BufferedReader buff = new BufferedReader(new FileReader(new File("diary.txt")));
            String tempLine;
            while ((tempLine = buff.readLine()) != null) {
                diaryText.append(tempLine).append("<br>");
            }
            buff.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));) { //input stream created from socket

            String headerLine = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(headerLine); //constructor
            String httpMethod = tokenizer.nextToken();

            if (httpMethod.equals("GET")) {
                System.out.println("Get method processed");
                String httpQueryString = tokenizer.nextToken();

                StringBuilder responseBuffer = new StringBuilder();
                responseBuffer
                        .append("<html><link rel=\"stylesheet\" href=\"https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css\">\n"
                                + "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"><script src=\"https://code.jquery.com/jquery-3.1.1.js\"></script><script src=\"https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js\"></script>"
                                + "<head><title>Best Page in the Universe</title></head><center><h1 style=\"color: white\">Secret Diary Homepage</h1><br>")
                        .append("<body style=\"background-color: #405060; padding: 100px\">")
                        .append("<div style=\"color: white\" id=\"diaryContents\"><b>Diary Contents:</b><BR>")
                        .append(diaryText).append("</div>");

                String buttonForm = "<form style=\"padding: 50px\">\n"
                        + "<textarea id=\"newSubmission\" class=\"form-control\" rows=\"4\" style=\"width: 150px\" name=\"diary\" placeholder=\"Enter a new diary entry here...\"></textarea>"
                        + "<input id=\"submitButton\" class=\"btn btn-primary\" style=\"margin-top: 20px\" value=\"Submit\">"
                        + "</form>";
                responseBuffer.append(buttonForm);
                String post = "<script>$(\"#submitButton\").click(function(){"
                        + "console.log('posting now');"
                        + "$.ajax({"
                        + "     url: 'localhost',"
                        + "     method: 'POST',"
                        + "     data: $(\"#newSubmission\").val(),"
                        + "     dataType: 'text/html',"
                        + "     timeout: 2500,"
                        + "     success: function(data){"
                                    +"console.dir(data);"
                        + "         $(\"#diaryContents\").append(data);},"
                        + "     error: function(data){console.log('something went wrong');"
                        + "             location.reload();},"
                        + "     after: function(){location.reload();} "
                        + "});});</script>";
                responseBuffer.append(post);
                responseBuffer.append("</body></html>");
                sendResponse(socket, 200, responseBuffer.toString());
            } else if (httpMethod.equals("POST")) {
                    System.out.println("POST HITTING");
                StringBuilder responseBuffer = new StringBuilder();
                String line;
                StringBuilder body = new StringBuilder();
                while (!in.readLine().isEmpty()) {
                }
                while((line = in.readLine())!=null)
                {
                    body.append(line);
                    if(line.isEmpty())
                        break;
                }
                System.out.println("out of fiorst");
                if(body != null)
                {
                    StringBuilder addEntry = new StringBuilder();
                    addEntry.append("\r\n");
                    addEntry.append("Date: " + new Date() + "\r\n");
                    addEntry.append("Entry: " + body.toString() + "\r\n");
                    
//                    try (PrintWriter fileout = new PrintWriter(new FileOutputStream(new File("diary.txt")),true)) {
//                            fileout.append(addEntry.toString());
//                            fileout.flush();
//                        }
                    //Here true is to append the content to file
    	FileWriter fw = new FileWriter(new File("diary.txt"),true);
    	//BufferedWriter writer give better performance
    	BufferedWriter bw = new BufferedWriter(fw);
    	bw.append(addEntry.toString());
    	//Closing BufferedWriter Stream
    	bw.close();
                    //responseBuffer.append(addEntry.toString());
                }
                //sendResponse(socket, 200, "Yup");

            } else {
                System.out.println("The HTTP method is not recognized");
                sendResponse(socket, 405, "Method Not Allowed");
            }
            /*
            Additional lines could be processed here by using:
            String httpQueryString = tokenizer.nextToken();
            This allows you to read more from the line.
            Would like want to move GET into a method so the entire length 
            would be processed
            
            I agree, I think that we should make GET its own method.
            The assignment needs us to Post strings to make a diary, and then GET
            the entire page. - Dan
             */

        } catch (Exception e) {
            
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
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());) {
            if (statusCode == 200) {
                statusLine = "HTTP/1.0 200 OK" + "\r\n";
                String contentLengthHeader = "Content-Length: "
                        + responseString.length() + "\r\n";
                out.writeBytes(statusLine);             //status line
                out.writeBytes(serverHeader);           //header
                out.writeBytes(contentTypeHeader);      //header
                out.writeBytes(contentLengthHeader);    //header
                out.writeBytes("\r\n");                 //blank line
                out.writeBytes(responseString);         //message body
            } else if (statusCode == 405) {
                statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
                out.writeBytes(statusLine);             //status line
                out.writeBytes("\r\n");                 //blank line
            } else {
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
