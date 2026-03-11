package com.omnistrate;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.omnistrate.licensing.validation.Validator;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpServerApp {

    private static String dbEndpoint1;
    private static String dbEndpoint2;
    private static String redisEndpoint;
    private static String pubsubId;

    public static void main(String[] args) throws IOException {
        // Check if terraform-provisioned endpoints are expected
        boolean useTerraform = "true".equalsIgnoreCase(System.getenv("USE_TERRAFORM"));

        if (useTerraform) {
            // Validate required endpoint environment variables from ConfigMap
            dbEndpoint1 = getRequiredEnv("dbEndpoint1");
            dbEndpoint2 = getRequiredEnv("dbEndpoint2");
            redisEndpoint = getRequiredEnv("redisEndpoint");
            pubsubId = getRequiredEnv("pubsubId");

            System.out.println("Terraform endpoints configured:");
            System.out.println("  dbEndpoint1:    " + dbEndpoint1);
            System.out.println("  dbEndpoint2:    " + dbEndpoint2);
            System.out.println("  redisEndpoint:  " + redisEndpoint);
            System.out.println("  pubsubId:       " + pubsubId);
        } else {
            System.out.println("useTerraform is disabled, skipping terraform endpoint validation");
        }

        System.out.println("Starting HTTP server on port 8080");
        // Create a simple HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new RootHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static String getRequiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                "Required environment variable '" + name + "' is not set or is empty. " +
                "Ensure the ConfigMap 'licensing-example-java-config' is properly configured."
            );
        }
        return value;
    }

    public static String getDbEndpoint1() {
        return dbEndpoint1;
    }

    public static String getDbEndpoint2() {
        return dbEndpoint2;
    }

    public static String getRedisEndpoint() {
        return redisEndpoint;
    }

    public static String getPubsubId() {
        return pubsubId;
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
