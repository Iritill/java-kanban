package Service.HttpTaskManager.HandlersForTasks;

import Service.FileBackedManager.Exception.TaskValidException;
import Service.HttpTaskManager.TaskNotFoundException;
import Service.Interface.TaskManager;
import Tasks.SubTask;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HandlerSubTask extends BaseHandlerForTask implements HttpHandler {

    public HandlerSubTask(TaskManager taskManager, Gson gson){
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
                    if (splitPath[1].equals("subtasks") && httpExchange.getRequestURI().getQuery() == null) {
                        String response = gson.toJson(manager.getAllSubTasks());
                        sendText(httpExchange, response, 200);
                        return;
                    } else if (splitPath[1].equals("subtasks") && httpExchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        String response = gson.toJson(manager.getSubTask(id));
                        sendText(httpExchange, response, 200);
                        return;
                    }
                }
                case "POST": {
                    if (splitPath[1].equals("subtasks")) {
                        String requestBody = readText(httpExchange);
                        SubTask subtask = gson.fromJson(requestBody, SubTask.class);
                        Integer id = subtask.getId();

                        if (id != 0) {
                            manager.update(subtask);
                            String response = "Успешное обновление задачи";
                            sendText(httpExchange, response, 201);
                        } else {
                            int subtask1 = manager.createSubTask(subtask);
                            String response = gson.toJson(subtask);
                            sendText(httpExchange, response, 201);
                        }
                        return;
                    }
                }
                case ("DELETE"): {
                    if (splitPath[1].equals("subtasks") && httpExchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        manager.clearByIdSubTask(id);
                        sendText(httpExchange, "", 200);
                        return;
                    }
                }
            }
        } catch (TaskNotFoundException e) {
            errorText(httpExchange, "Ошибка! задача не найдена", 404);
        } catch (TaskValidException e) {
            errorText(httpExchange, "Ошибка! Задача пересекается!", 406);
        } finally {
            httpExchange.close();
        }
    }
}
