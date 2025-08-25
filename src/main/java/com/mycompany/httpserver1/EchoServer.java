//package com.mycompany.httpserver1;
//
//import java.io.*;
//import java.net.*;
//
//public class EchoServer {
//
//    private int port = 35000;
//
//    public void start() throws IOException {
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            System.out.println("EchoServer listening on port " + port);
//
//            while (true) {
//                Socket clientSocket = serverSocket.accept();
//                Service service = new Service(clientSocket);
//                new Thread(service).start();
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        EchoServer server = new EchoServer();
//        server.start();
//    }
//}
