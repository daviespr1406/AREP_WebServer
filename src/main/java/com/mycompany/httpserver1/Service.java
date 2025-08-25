package com.mycompany.httpserver1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Service {
    private static final Map<String, BiFunction<HttpRequest, HttpResponse, String>> getRoutes = new HashMap<>();
    private static String staticFolder = "public";

    public static void staticfiles(String folder) {
        staticFolder = folder;
    }

    public static void get(String path, BiFunction<HttpRequest, HttpResponse, String> handler) {
        getRoutes.put("GET " + path, handler);
    }

    public static String getStaticFolder() {
        return staticFolder;
    }

    public static BiFunction<HttpRequest, HttpResponse, String> match(String method, String path) {
        return getRoutes.get(method + " " + path);
    }

    public static String renderStaticFile(String filename) {
        try {
            Path filePath = Paths.get("public", filename);
            if (Files.exists(filePath)) {
                return Files.readString(filePath);
            } else {
                return "<h1>404 - File Not Found</h1>";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "<h1>500 - Internal Server Error</h1>";
        }
    }
}


