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

public class Lab4Team1
{
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException
    {
        WebServer webServer = new WebServer();
        webServer.start();
    }
    
}
