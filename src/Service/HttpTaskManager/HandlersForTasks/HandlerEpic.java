package Service.HttpTaskManager.HandlersForTasks;

import Service.FileBackedManager.Exception.TaskValidException;
import Service.HttpTaskManager.TaskNotFoundException;
import Service.Interface.TaskManager;
import Tasks.Epic;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class HandlerEpic extends BaseHandlerForTask implements HttpHandler {
    public HandlerEpic(TaskManager taskManager, Gson gson){
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
                    if (splitPath[1].equals("epics") && splitPath.length == 2 && httpExchange.getRequestURI().getQuery() == null) {
                        String response = gson.toJson(manager.getAllEpics());
                        sendText(httpExchange, response, 200);
                        return;
                    } else if (splitPath[1].equals("epics") && splitPath.length == 2 && httpExchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        String response = gson.toJson(manager.getEpic(id));
                        sendText(httpExchange, response, 200);
                        return;
                    } else if (splitPath[1].equals("epics") && splitPath[2].equals("subtasks") && httpExchange.getRequestURI().getQuery() != null){
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        String response = gson.toJson(manager.getAllSubTaskByEpic(id));
                        sendText(httpExchange, response, 200);
                        return;

                    }
                }
                case "POST": {
                    if (splitPath[1].equals("epics")) {
                        String requestBody = readText(httpExchange);
                        Epic epic = gson.fromJson(requestBody, Epic.class);

                        Integer epicId = epic.getId();
                        int epic1 = manager.createEpic(epic);
                        String response = gson.toJson(epic);
                        sendText(httpExchange, response, 201);

                        return;
                    }
                }
                case ("DELETE"): {
                    if (splitPath[1].equals("epics") && httpExchange.getRequestURI().getQuery() != null) {
                        int id = Integer.parseInt(httpExchange.getRequestURI().getQuery().replaceFirst("id=", ""));
                        manager.clearByIdEpic(id);
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
