package com.mycompany.httpserver1;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class Httpserverasync {
    private int port;
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    public Httpserverasync(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String line = in.readLine();
            if (line == null || line.isEmpty()) return;

            String[] parts = line.split(" ");
            String method = parts[0];
            String fullPath = parts[1];
            String path = fullPath.split("\\?")[0];
            Map<String, String> queryParams = parseQuery(fullPath);

            HttpRequest request = new HttpRequest(method, path, queryParams);
            HttpResponse response = new HttpResponse();

            var handler = Service.match(method, path);

            if (handler != null) {
                String body = handler.apply(request, response);
                response.setBody(body);
            } else {
                // Servir archivos estáticos
                Path filePath = Paths.get(Service.getStaticFolder(), path);
                if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                    String content = Files.readString(filePath);
                    response.setContentType(getMimeType(filePath.toString()));
                    response.setBody(content);
                } else {
                    response.setStatus(404);
                    response.setBody("404 Not Found");
                }
            }

            out.write(response.build().getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> parseQuery(String fullPath) {
        Map<String, String> query = new HashMap<>();
        if (fullPath.contains("?")) {
            String q = fullPath.split("\\?", 2)[1];
            for (String param : q.split("&")) {
                String[] kv = param.split("=");
                if (kv.length == 2) {
                    query.put(kv[0], kv[1]);
                }
            }
        }
        return query;
    }

    private String getMimeType(String filename) {
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        return "text/plain";
    }
}
