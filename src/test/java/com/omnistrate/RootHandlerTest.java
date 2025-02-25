package com.omnistrate;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.omnistrate.HttpServerApp.RootHandler;

public class RootHandlerTest {

    private RootHandler rootHandler;
    private HttpExchange httpExchange;

    @BeforeEach
    public void setUp() {
        rootHandler = new HttpServerApp.RootHandler();
        httpExchange = mock(HttpExchange.class);
    }

    @Test
    public void testHandleValidLicense() throws IOException {
        // Mock the HttpExchange
        when(httpExchange.getRequestURI()).thenReturn(URI.create("/"));
        when(httpExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());

        // Call the handle method
        rootHandler.handle(httpExchange);

        // Verify the response
        OutputStream os = httpExchange.getResponseBody();
        String response = os.toString();
        assertTrue(response.contains("<h1>Error</h1>"));
    }
}
