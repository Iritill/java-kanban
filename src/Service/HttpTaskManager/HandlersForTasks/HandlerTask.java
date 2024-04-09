package Service.HttpTaskManager.HandlersForTasks;

import Service.FileBackedManager.Exception.TaskValidException;
import Service.HttpTaskManager.TaskNotFoundException;
import Service.Interface.TaskManager;
import Tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HandlerTask extends BaseHandlerForTask implements HttpHandler {
    public HandlerTask(TaskManager taskManager, Gson gson){
        super(taskManager, gson);
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String[] splitPath = path.split("/");
        String method = httpExchange.getRequestMethod();
        try {
            switch (method) {
                case "GET": {
                    if (splitPath[1].equals("tasks") && httpExchange.getRequestURI().getQuery() == null) {
                        String response = gson.toJson(manager.getAllTasks());
                        sendText(httpExchange, response, 200);
                        return;
                    } else if (splitPath[1].equals("tasks") && httpExchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        String response = gson.toJson(manager.getTask(id));
                        sendText(httpExchange, response, 200);
                        return;
                    }
                }
                case "POST": {
                    if (splitPath[1].equals("tasks")) {
                        String requestBody = readText(httpExchange);
                        Task task = gson.fromJson(requestBody, Task.class);
                        Integer taskId = task.getId();
                        if (taskId != 0) {
                            manager.update(task);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response, 201);
                        } else {
                            int task1 = manager.createTask(task);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response, 201);
                        }
                        return;
                    }
                }
                case ("DELETE"): {
                    if (splitPath[1].equals("tasks") && httpExchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        manager.clearByIdTask(id);
                        sendText(httpExchange, "", 200);
                        return;
                    }
                }

            }
        } catch (TaskNotFoundException e) {
            errorText(httpExchange, "Ошибка! Задача не найдена", 404);
        } catch (TaskValidException e) {
            errorText(httpExchange, "Ошибка! Задача пересекается!", 406);
        } finally {
            httpExchange.close();
        }
    }
}
