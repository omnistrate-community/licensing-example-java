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
 	    // TODO: Replace with your own values
	    // Unique ID provided by Omnistrate for your organization (can be found in the user profile)
        private static final String orgID = "org-4xihABXKyq";
	    // Unique Product ID provided that can be configured in Omnistrate, by default it is the Product Plan ID
        private static final String productID = "PRODUCT-SAMPLE-JAVA-SKU-UNIQUE-VALUE";
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Placeholder</h1>";
            try {
                // This is the validation method that can be used to validate the license for a product in Omnistrate
                // With a simple call to this method, you can:
                // - confirm the validity of the certificate that signed the license
                // - validate the license signature
                // - validate the license expiration date
                // - validate the unique product id configured in omnistrate maps with the product your organization
                // - validate that the injected variable containing the instance-id maps with the existing license
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
