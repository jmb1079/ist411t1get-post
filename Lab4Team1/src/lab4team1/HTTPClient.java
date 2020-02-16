package lab4team1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.stream.IntStream;

public class HTTPClient
{

    private Scanner scnr;
    private static InetAddress serverInetAddress;
    private static final int PORTNUMBER = 8080;
    private static final String INVALIDENTRY = "An invalid selection was made";
    private static final String NEWLINE = "\r\n";
    private static final int[] validEntries =
    {
        1, 2, 3
    };

    public static void main(String[] args)
    {
        HTTPClient httpClient = new HTTPClient();
        httpClient.start();
    }

    public HTTPClient()
    {
        scnr = new Scanner(System.in);
        try
        {
            serverInetAddress = InetAddress.getByName("127.0.0.1");
        }
        catch (UnknownHostException uhEx)
        {
            System.out.println("Unknown host");
            uhEx.printStackTrace();
        }
    }

    private void start()
    {
        String input;
        System.out.println("HTTP Client Started");
        boolean quit = false;
        while (!quit)
        {
            try
            {
                System.out.println("Enter 1 for GET, 2 for POST and 3 for quit");
                if (scnr.hasNextInt())
                {
                    Integer choice = (scnr.nextInt());
                    scnr.nextLine(); //throw away the \n not consumed by nextInt()
                    if (IntStream.of(validEntries).noneMatch(x -> x == choice))
                    {
                        System.out.println(INVALIDENTRY + NEWLINE);
                    }
                    else if (choice.equals(3))
                    {
                        System.out.println("Good Bye");
                        quit = true;
                    }
                    else
                    {
                        Socket connection = new Socket(serverInetAddress, PORTNUMBER);
                        try (OutputStream out = connection.getOutputStream();
                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())))
                        {
                            if (choice.equals(1))
                            {
                                sendGet(out);
                                System.out.println(getResponse(in));
                            }
                            else if (choice.equals(2))
                            {
                                System.out.println("Enter Diary message");
                                input = scnr.nextLine();
                                sendPost(input, out);
                                System.out.println(getResponse(in));
                            }
                        }
                    }
                }
                else
                {
                    System.out.println(INVALIDENTRY + NEWLINE);
                    scnr.nextLine(); //throw away the \n not consumed by nextInt()
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void sendGet(OutputStream out)
    {
        try
        {
            out.write((HttpMethod.GET.toString() + " /default" + NEWLINE).getBytes());
            out.write(("User-Agent: Mozilla/5.0" + NEWLINE).getBytes());
            out.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
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
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

    public void sendPost(String msg, OutputStream out)
    {
        try
        {
            out.write((HttpMethod.POST.toString() + " /default\r\n").getBytes());
            out.write((msg + "\r\n").getBytes());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public Scanner getScnr()
    {
        return scnr;
    }

    public void setScnr(Scanner scnr)
    {
        this.scnr = scnr;
    }

    public int getPORTNUMBER()
    {
        return PORTNUMBER;
    }
}
