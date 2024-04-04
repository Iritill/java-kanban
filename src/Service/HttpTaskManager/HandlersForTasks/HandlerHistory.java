package Service.HttpTaskManager.HandlersForTasks;

import Service.Interface.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HandlerHistory extends BaseHandlerForTask implements HttpHandler {

    public HandlerHistory(TaskManager taskManager, Gson gson){
        super(taskManager, gson);
    }

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                if (splitPath[1].equals("history") && exchange.getRequestURI().getQuery() == null) {
                    String response = gson.toJson(manager.getHistory());
                    sendText(exchange, response, 200);
                    return;
                }
            }
        } finally {
            exchange.close();
        }
    }
}
