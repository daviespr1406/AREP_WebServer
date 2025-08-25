package com.mycompany.httpserver1;

public class WebApplication {

    public static void main(String[] args){
        runServer(args);
    }


    private static void runServer(String[] args) {
        Service.staticfiles("public");


        Service.get("/", (req, res) -> {
            res.setContentType("text/plain");
            return "Hello from root!";
        });

        Service.get("/index", (req, res) -> {
            res.setContentType("text/html");
            return Service.renderStaticFile("index.html");
        });

        Service.get("/hello", (req, res) -> {
            res.setContentType("text/plain");
            return "Hello World!";
        });

        Service.get("/helloName", (req, res) -> {
            String name = req.getQueryParam("name");
            res.setContentType("text/plain");
            return "Hello " + (name != null ? name : "anonymous");
        });

        int port = 35000;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port. Using default 35000.");
            }
        }

        Httpserverasync server = new Httpserverasync(port);
        server.start();
    }
}

