package Service.HttpTaskManager;

import Service.FileBackedManager.Exception.TaskValidException;
import com.google.gson.*;
import com.google.gson.GsonBuilder;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import Service.Interface.*;
import Service.InMemoryManager.*;
import Tasks.Task;
import Tasks.Epic;
import Tasks.SubTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    public final int PORT = 8080;
    private Gson gson;
    private static HttpServer server;
    private TaskManager manager;


    public void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public HttpTaskServer() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                ;
        gson = gsonBuilder.create();
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        this.manager = new InMemoryTaskManager();
        server.createContext("/tasks", new handlerTask());
        server.createContext("/subtasks", new handlerSubtask());
        server.createContext("/epics", new handlerEpic());
        server.createContext("/epics/subtasks", new handlerEpic());
        server.createContext("/history", new handlerHistory());
        server.createContext("/prioritized", new handler());
        server.start();
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                ;
        gson = gsonBuilder.create();
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        this.manager = manager;
        server.createContext("/tasks", new handlerTask());
        server.createContext("/subtasks", new handlerSubtask());
        server.createContext("/epics", new handlerEpic());
        server.createContext("/epics/subtasks", new handlerEpic());
        server.createContext("/history", new handlerHistory());
        server.createContext("/prioritized", new handler());
        server.start();
    }



    public TaskManager getManager() {
        return manager;
    }


    class handlerTask implements HttpHandler {
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

    class handlerEpic implements HttpHandler {
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

    class handlerSubtask implements HttpHandler {
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

    public void errorText(HttpExchange exchange, String response, int code) throws IOException {
        String json = gson.toJson(response);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(bytes);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }


    class handler implements HttpHandler {
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

    class handlerHistory implements HttpHandler {
        @Override
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

    public void sendText(HttpExchange exchange, String response, int code) throws IOException {
        byte[] bytes = response.getBytes(Charset.defaultCharset());
        exchange.sendResponseHeaders(code, bytes.length);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(bytes);
    }


    public void start() {
        System.out.println("HTTP Task Server запущен");
        server.start();
    }

    public static void stop(int delay) {
        server.stop(delay);
        System.out.println("HTTP Task Server остановлен");
    }

    public static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss.SSSSSSSSS");


        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            if (localDateTime != null) {
                jsonWriter.value(localDateTime.format(formatter));
            } else {
                jsonWriter.value("null");
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            String str = jsonReader.nextString();
            if (str == null || "null".equals(str)) {
                return null;
            } else {
                LocalDateTime time = LocalDateTime.parse(str, formatter);
                return time;
            }
        }
    }

    public static class DurationAdapter extends TypeAdapter<Duration> {


        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            if (duration != null) {
                jsonWriter.value(duration.toMinutes());
            } else {
                jsonWriter.nullValue();
            }
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else {
                return Duration.ofMinutes(jsonReader.nextLong());
            }
        }
    }

}
