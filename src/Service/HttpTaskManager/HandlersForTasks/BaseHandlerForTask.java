package Service.HttpTaskManager.HandlersForTasks;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import Service.Interface.TaskManager;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseHandlerForTask implements HttpHandler {
    TaskManager manager;
    Gson gson;

    public BaseHandlerForTask(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
    public void errorText(HttpExchange exchange, String response, int code) throws IOException {
        String json = gson.toJson(response);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(bytes);
    }

    public String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }
    public void sendText(HttpExchange exchange, String response, int code) throws IOException {
        byte[] bytes = response.getBytes(Charset.defaultCharset());
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(bytes);
    }
}
