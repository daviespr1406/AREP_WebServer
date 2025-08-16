package com.mycompany.httpserver1;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Httpserverasync {
    private static final int PORT = 35000;
    private static final String WEB_ROOT = "public";
    private static final String DEFAULT_FILE = "/index.html";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor escuchando en puerto " + PORT + "...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error con cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo abrir el puerto " + PORT + ": " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) throws IOException {
        socket.setSoTimeout(5000);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            System.out.println(">> " + requestLine);

            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
            }

            StringTokenizer tokens = new StringTokenizer(requestLine);
            String method = tokens.nextToken();
            String rawPath = tokens.nextToken(); 

            if (rawPath.equals("/")) rawPath = DEFAULT_FILE;

            if (rawPath.startsWith("/hello")) {
                handleHello(rawPath, out);
            } else if (rawPath.startsWith("/hellopost")) {
                handleHelloPost(rawPath, out, method);
            } else {
                serveStaticFile(rawPath, out);
            }
        }
    }

    private static void handleHello(String rawPath, OutputStream out) throws IOException {
        Map<String, String> q = parseQueryParams(rawPath);
        String name = q.getOrDefault("name", "World");
        String body = "Hello, " + name + "!";
        sendResponse(out, 200, "OK", "text/plain; charset=utf-8", body.getBytes(StandardCharsets.UTF_8));
    }

    private static void handleHelloPost(String rawPath, OutputStream out, String method) throws IOException {
        Map<String, String> q = parseQueryParams(rawPath);
        String name = q.getOrDefault("name", "World");
        String body = "Hello from POST, " + name + "!";
        sendResponse(out, 200, "OK", "text/plain; charset=utf-8", body.getBytes(StandardCharsets.UTF_8));
    }

    private static void serveStaticFile(String rawPath, OutputStream out) throws IOException {
        Path requested = Paths.get(WEB_ROOT, rawPath).normalize();
        if (!requested.startsWith(Paths.get(WEB_ROOT))) {
            sendForbidden(out);
            return;
        }

        if (!Files.exists(requested) || Files.isDirectory(requested)) {
            sendNotFound(out);
            return;
        }

        String mime = getMimeType(requested);
        byte[] bytes = Files.readAllBytes(requested);
        sendResponse(out, 200, "OK", mime, bytes);
    }

    private static String getMimeType(Path path) throws IOException {
        String mime = Files.probeContentType(path);
        if (mime != null) return mime;
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".html") || name.endsWith(".htm")) return "text/html; charset=utf-8";
        if (name.endsWith(".css")) return "text/css; charset=utf-8";
        if (name.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".gif")) return "image/gif";
        if (name.endsWith(".svg")) return "image/svg+xml";
        if (name.endsWith(".ico")) return "image/x-icon";
        return "application/octet-stream";
    }

    private static Map<String, String> parseQueryParams(String rawPath) {
        Map<String, String> map = new HashMap<>();
        int qIdx = rawPath.indexOf('?');
        if (qIdx < 0) return map;
        String query = rawPath.substring(qIdx + 1);
        for (String pair : query.split("&")) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split("=", 2);
            String k = urlDecode(kv[0]);
            String v = kv.length > 1 ? urlDecode(kv[1]) : "";
            map.put(k, v);
        }
        return map;
    }

    private static String urlDecode(String s) {
        try {
            return java.net.URLDecoder.decode(s, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    private static void sendNotFound(OutputStream out) throws IOException {
        String html = "<h1>404 Not Found</h1>";
        sendResponse(out, 404, "Not Found", "text/html; charset=utf-8", html.getBytes(StandardCharsets.UTF_8));
    }

    private static void sendForbidden(OutputStream out) throws IOException {
        String html = "<h1>403 Forbidden</h1>";
        sendResponse(out, 403, "Forbidden", "text/html; charset=utf-8", html.getBytes(StandardCharsets.UTF_8));
    }

    private static void sendResponse(OutputStream out, int code, String status, String contentType, byte[] body) throws IOException {
        String headers =
            "HTTP/1.1 " + code + " " + status + "\r\n" +
            "Content-Type: " + contentType + "\r\n" +
            "Content-Length: " + body.length + "\r\n" +
            "Connection: close\r\n" +
            "\r\n";
        out.write(headers.getBytes(StandardCharsets.US_ASCII));
        out.write(body);
    }
}
