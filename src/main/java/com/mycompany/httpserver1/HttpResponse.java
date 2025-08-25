package com.mycompany.httpserver1;

public class HttpResponse {
    private int status = 200;
    private String contentType = "text/plain";
    private String body = "";

    public void setStatus(int status) {
        this.status = status;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String build() {
        return "HTTP/1.1 " + status + " OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + body.getBytes().length + "\r\n"
                + "\r\n"
                + body;
    }
}
