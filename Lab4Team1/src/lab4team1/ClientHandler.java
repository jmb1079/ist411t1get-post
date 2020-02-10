/* 
 Project: Lab 4 - Group
 Purpose Details: GET and POST application
 Course: IST 411
 Author: Ryan Urbanski, Albana Beqo, James Bristow II, Logan Pratt, Fred Aaron
 Date Developed: 2/9/2020
 Last Date Changed:2/9/2020
 Revision: 1
 */
package lab4team1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable
{
    private final Socket socket;
    private static final String HANDLERSTARTED = "\nClientHandler Started for ";
    private static final String HANDLERTERMINATED = "ClientHandler Terminated for ";
    private static final String GETPROCESSED = "Get method processed";
    private static final String UNRECOGNIZEDMETHOD = "The HTTP method is not recognized";
    private static final String WELCOME = "<html><h1>WebServer Home Page... </h1><br><b>Welcome to my web server!</b><br></html>";
    private static final String STATUS = "HTTP/1.0 %d %s";
    private static final String NEWLINE = "\r\n";
    private static final String METHODNOTALLOWED = "Method Not Allowed";
    private static final String OK = "OK";
    private static final int NOTFOUNDCODE = 404;
    private static final String NOTFOUND = "NOT FOUND";
    private static final String CONTENTLENGTH = "Content-Length: ";
    private static final String SERVERHEADER = "Server: WebServer" + NEWLINE;
    private static final String CONTENTTYPEHEADER = "Content-Type: text/html" + NEWLINE;
            
    public ClientHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        System.out.println(HANDLERSTARTED);
        handleRequest(this.socket);
        System.out.println(HANDLERTERMINATED);
    }

    private void handleRequest(Socket socket)
    {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));)
        {
            String headerLine = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            if(httpMethod.equals(HttpMethod.GET.name()))
            {
                processGetRequest(tokenizer);
            }
            else
            {
                System.out.println(UNRECOGNIZEDMETHOD);
                sendResponse(socket, 405, METHODNOTALLOWED);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void processGetRequest(StringTokenizer tokenizer)
    {
        System.out.println(GETPROCESSED);
        String httpQuery = tokenizer.nextToken();
        StringBuilder responseBuffer = new StringBuilder();
        responseBuffer.append(WELCOME);
        sendResponse(socket,200,responseBuffer.toString());
    }

    public void sendResponse(Socket socket, int statusCode, String responseString)
    {
        //this seems dirty. Move response construction to its own object?
        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());)
        {
            if (statusCode == 200)
            {
                out.writeBytes(String.format(STATUS,statusCode,OK) + NEWLINE);
                out.writeBytes(SERVERHEADER);
                out.writeBytes(CONTENTTYPEHEADER);
                out.writeBytes(NEWLINE);
                out.writeBytes(responseString);
            }
            else if (statusCode == 405)
            {
                out.writeBytes(String.format(STATUS,statusCode,METHODNOTALLOWED)+NEWLINE);
                out.writeBytes(NEWLINE);
            }
            else
            {
                out.writeBytes(String.format(STATUS,NOTFOUNDCODE,NOTFOUND)+NEWLINE);
                out.writeBytes(NEWLINE);
            }
            out.close();
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
        }
    }
}
