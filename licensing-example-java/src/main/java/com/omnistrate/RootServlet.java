package com.omnistrate;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.omnistrate.licensing.validation.Validator;

@WebServlet("/")
public class RootServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");


        // Validate license key for product
        // This is the validation method that can be used to validate license key for a product in Omnistrate
        // With a simple call to this method, you can:
        // - confirm the validity of the certificate that signed the license
        // - validate the license signature
        // - validate the license expiration date
        // - validate the unique sku configured in omnistrate maps with the product
        // - validate that the injected variable containing the instance-id maps with the license
        try {
            boolean isValid = Validator.validateLicenseForProduct("PRODUCT-SAMPLE-JAVA-SKU-UNIQUE-VALUE"); 
            if (isValid) {
                response.getWriter().println("<h1>Success</h1><p>License is valid</p>");
                response.getWriter().println("License validation for product succeeded");
            } else {
                response.getWriter().println("<h1>Error</h1><p>License is invalid/p>");
            }
        } catch (Exception e) {
            response.getWriter().println("<h1>Error</h1><p>" + e.getMessage() + "/p>");
        }
        response.getWriter().println("<h1>Welcome to the Root Path!</h1>");
    }
}

