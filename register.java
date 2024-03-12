import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class register {

    private static final Map<String, String> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Create HTTP server listening on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // Create context for register page
        server.createContext("/register", new RegisterHandler());

        // Start server
        server.start();
        System.out.println("Server started on port 8000");
    }

    // Handler for register page
    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Handle POST request for registration
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

            // Dummy registration logic (replace with actual logic)
            boolean isRegistered = registerUser(username, password);

            // Prepare response
            String response;
            if (isRegistered) {
                response = "Registration successful";
                exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            } else {
                response = "Username already exists";
                exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
            }

            // Send response
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes(StandardCharsets.UTF_8));
            responseBody.close();
        }

        private boolean registerUser(String username, String password) {
            // Dummy registration logic (replace with actual logic)
            if (!users.containsKey(username)) {
                users.put(username, password);
                return true;
            } else {
                return false;
            }
        }
    }
}