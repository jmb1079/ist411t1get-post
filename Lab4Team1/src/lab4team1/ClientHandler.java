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
   private static final String POSTPROCESSED = "Post method processed";
   private static final String UNRECOGNIZEDMETHOD = "The HTTP method is not recognized";
   private static final String WELCOME = "<html><h1>WebServer Home Page... </h1><br><b>Welcome to my web server!</b><br></html>";
   private static final String STATUS = "HTTP/1.0 %d %s";
   private static final String NEWLINE = "\r\n";
   private static final String METHODNOTALLOWED = "Method Not Allowed";
   private static final String OK = "Message OK";
   private static final int NOTFOUNDCODE = 404;
   private static final String NOTFOUND = "NOT FOUND";
   private static final String CONTENTLENGTH = "Content-Length: ";
   private static final String SERVERHEADER = "Server: WebServer" + NEWLINE;
   private static final String CONTENTTYPEHEADER = "Content-Type: text/html" + NEWLINE;
   private Diary diary = new Diary();

   public ClientHandler(Socket socket, Diary diary)
   {
      this.socket = socket;
      this.diary = diary;
   }

   @Override
   public void run()
   {
      System.out.println(HANDLERSTARTED);
      handleRequest(this.socket);
      System.out.println(HANDLERTERMINATED);
   }

   public void handleRequest(Socket socket)
   {
      try (BufferedReader in = new BufferedReader(
              new InputStreamReader(socket.getInputStream()));)
      {
         String headerLine = in.readLine();
         StringTokenizer tokenizer
                 = new StringTokenizer(headerLine);
         String httpMethod = tokenizer.nextToken();
         StringBuilder responseBuffer = new StringBuilder();

         if (httpMethod.equals("GET"))
         {
            System.out.println(GETPROCESSED);
            responseBuffer.append("Current Diary\r\n" + diary);
            sendResponse(socket, 200, responseBuffer.toString(), "GET");
         }
         else if (httpMethod.equals("POST"))
         {
            System.out.println(POSTPROCESSED);
            responseBuffer.append("Diary Updated\n");
            String response = in.readLine();
            diary.addDiary(response);
            sendResponse(socket, 200, responseBuffer.toString(), "POST");
         }
         else
         {
            System.out.println("The HTTP method is not recognized");
            sendResponse(socket, 405, "Method Not Allowed", "405");
         }
         in.close();
      } catch (Exception e)
      {
         e.printStackTrace();
      }

   }

   public void sendResponse(Socket socket, int statusCode, String responseString, String command)
   {
      try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());)
      {
         if (statusCode == 200)
         {
            if (command.equals("GET"))
            {
               out.writeBytes(String.format(STATUS, statusCode, OK) + NEWLINE);
               out.writeBytes("Get Response Code : 200 ");
               out.writeBytes(NEWLINE);
               out.writeBytes("Get Reponse : " + OK + "\r\n");
               out.writeBytes(NEWLINE);
               out.writeBytes(responseString);
            }
            if (command.equals("POST"))
            {
               out.writeBytes(String.format(STATUS, statusCode, OK) + NEWLINE);
               out.writeBytes("Post Response Code : 200 ");
               out.writeBytes(NEWLINE);
               out.writeBytes("Post Reponse : " + OK);
               out.writeBytes(NEWLINE);
            }

         }
         else if (statusCode == 405)
         {
            out.writeBytes(String.format(STATUS, statusCode, METHODNOTALLOWED) + NEWLINE);
            out.writeBytes(NEWLINE);
         }
         else
         {
            out.writeBytes(String.format(STATUS, NOTFOUNDCODE, NOTFOUND) + NEWLINE);
            out.writeBytes(NEWLINE);
         }
         out.close();
      } catch (IOException ioEx)
      {
         ioEx.printStackTrace();
      }
   }

   public Diary getDiary()
   {
      return diary;
   }

   public void setDiary(Diary diary)
   {
      this.diary = diary;
   }
}
