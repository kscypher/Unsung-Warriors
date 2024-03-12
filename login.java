import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class login {

    public static void main(String[] args) throws IOException {
        // Create HTTP server listening on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create context for login page
        server.createContext("/login", new LoginHandler());

        // Start server
        server.start();
        System.out.println("Server started on port 8000");
    }

    // Handler for login page
    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Handle POST request for login
            if ("POST".equals(exchange.getRequestMethod())) {
                handlePostRequest(exchange);
            } else {
                // Return 405 Method Not Allowed for other request methods
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            // Read request body
            InputStream requestBody = exchange.getRequestBody();
            StringBuilder requestBodyBuilder = new StringBuilder();
            int byteData;
            while ((byteData = requestBody.read()) != -1) {
                requestBodyBuilder.append((char) byteData);
            }
            String requestBodyString = requestBodyBuilder.toString();

            // Parse request body (assuming it's in format "username=username&password=password")
            String[] params = requestBodyString.split("&");
            String username = params[0].split("=")[1];
            String password = params[1].split("=")[1];

            // Dummy authentication logic (replace with actual logic)
            boolean isAuthenticated = "admin".equals(username) && "password".equals(password);

            // Prepare response
            String response;
            if (isAuthenticated) {
                response = "Login successful";
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            } else {
                response = "Invalid username or password";
                exchange.sendResponseHeaders(401, response.getBytes(StandardCharsets.UTF_8).length);
            }

            // Send response
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes(StandardCharsets.UTF_8));
            responseBody.close();
        }
    }
}