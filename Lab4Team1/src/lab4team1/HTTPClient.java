/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4team1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Frederick A. Aaron
 */
public class HTTPClient
{

   Scanner scnr = new Scanner(System.in);

   public HTTPClient()
   {
      String input;
      System.out.println("HTTP Client Started");
      boolean quit = false;

      {
         while (true)
         {
            try
            {
               InetAddress serverInetAddress = InetAddress.getByName("127.0.0.1");
               Socket connection = new Socket(serverInetAddress, 8080);
               try (OutputStream out = connection.getOutputStream();
                       BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())))
               {
                  //    while (!quit)
                  //  {
                  System.out.println("Enter 1 for GET and 2 for POST");
                  int choice = (scnr.nextInt());
                  if (choice == 1)
                  {
                     System.out.println(" before sendGet");
                     sendGet(out);
                     System.out.println(getResponse(in));
                  }
                  else if (choice == 2)
                  {
                     System.out.println("Enter Diary message");
                     input = scnr.next();
                     if (!(input.equals("quit")))
                     {
                        sendPost(input, out);
                        System.out.println(getResponse(in));
                     }
                     else
                     {
                        quit = true;
                     }
                  }
                  //   }
                  in.close();
               }
            } catch (IOException ex)
            {
               ex.printStackTrace();
            }
         }
      }
   }

   private void sendGet(OutputStream out)
   {
      try
      {
         System.out.println("at sendGet");
         out.write("GET /default\r\n".getBytes());
         System.out.println("sendGet 2");
         out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
         out.flush();
      } catch (IOException ex)
      {
         System.out.println("4" + ex);

         ex.printStackTrace();
      }
   }

   public static void main(String[] args)
   {
      new HTTPClient();
   }

   private String getResponse(BufferedReader in)
   {
      try
      {
         String inputLine;
         StringBuilder response = new StringBuilder();
         while ((inputLine = in.readLine()) != null)
         {
            response.append(inputLine).append("\n");
         }
         return response.toString();
      } catch (IOException ex)
      {

         System.out.println("1" + ex);

         ex.printStackTrace();
      }
      return "";
   }

   public void sendPost(String msg, OutputStream out)
   {
      try
      {
         out.write("POST /default\r\n".getBytes());
         out.write((msg + "\r\n").getBytes());
      } catch (IOException ex)
      {
         ex.printStackTrace();
         System.out.println("2" + ex);

      }
   }
}
