package com.mycompany.httpserver1;

import java.io.*;
import java.net.*;

public class EchoClient {

    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int portNumber = 35000;

        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("Respuesta del servidor: " + in.readLine());
                if ("Bye.".equalsIgnoreCase(userInput)) {
                    break;
                }
            }
        }
    }
}
