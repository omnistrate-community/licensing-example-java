package com.omnistrate;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.omnistrate.licensing.validation.Validator;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerApp {


    public static void main(String[] args) throws IOException {
        System.out.println("Starting HTTP server on port 8080");
        // Create a simple HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new RootHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class RootHandler implements HttpHandler {
        // Replace with your own values
        private static final String orgID = "org-4xihABXKyq";
        private static final String productID = "PRODUCT-SAMPLE-JAVA-SKU-UNIQUE-VALUE";
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Placeholder</h1>";
            try {
                boolean isValid = Validator.validateLicense(orgID, productID); 
                if (isValid) {
                    response = "<h1>Success</h1><p>License is valid</p>";
                } else {
                    response = "<h1>Error</h1><p>License is invalid</p>";
                }
            } catch (Exception e) {
                response = "<h1>Error</h1><p>" + e.getMessage() + "</p>";
            }

            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
