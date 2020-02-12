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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer
{
    private static final String SERVERSTARTED = "Web server started";
    private static final String WAITINGONCONNECTION = "Waiting for a client request";
    private static final String CONNECTED = "Connection made";
    private static final int PORTNUMBER = 8080;
    
    public WebServer()
    {
        System.out.println(SERVERSTARTED);
        
    }
    
    /**
     * Starts the web server servicing processes.
     * Moved from constructor due to thread being created
     * @throws java.io.IOException
     */
    public void start() throws IOException
    {
       Diary diary = new Diary();
        try (ServerSocket serverSocket = new ServerSocket(PORTNUMBER))
        {
            while(true)
            {
                System.out.println(WAITINGONCONNECTION);
                Socket remote = serverSocket.accept();
                System.out.println(CONNECTED);
                //new Thread(new ClientHandler(remote)).start();
                ClientHandler clientHandler = new ClientHandler(remote, diary);
                Thread client = new Thread(clientHandler);
                
                      
                client.start();
            }
        }
        catch (IOException ioEx)
        {
            System.out.println("3" + ioEx);
        }
    }
}
