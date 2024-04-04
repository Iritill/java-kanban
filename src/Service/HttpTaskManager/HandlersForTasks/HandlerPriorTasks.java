package Service.HttpTaskManager.HandlersForTasks;

import Service.Interface.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HandlerPriorTasks extends BaseHandlerForTask implements HttpHandler {
    public HandlerPriorTasks(TaskManager taskManager, Gson gson){
        super(taskManager, gson);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                if (splitPath[1].equals("prioritized") && exchange.getRequestURI().getQuery() == null) {
                    String response = gson.toJson(manager.getPrioritizedTasks());
                    sendText(exchange, response, 200);
                    return;
                }
            }
        } finally {
            exchange.close();
        }
    }
}
