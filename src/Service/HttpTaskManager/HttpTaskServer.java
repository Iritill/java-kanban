package Service.HttpTaskManager;

import Service.HttpTaskManager.HandlersForTasks.HandlerSubTask;
import Service.HttpTaskManager.HandlersForTasks.HandlerTask;
import com.google.gson.*;
import com.google.gson.GsonBuilder;


import com.sun.net.httpserver.HttpServer;

import Service.Interface.*;
import Service.InMemoryManager.*;
import Service.HttpTaskManager.HandlersForTasks.*;
import Service.HttpTaskManager.TypeAdapters.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {

    public static final int PORT = 8080;
    private static HttpServer server;
    private final TaskManager manager;


    public void main(String[] args) throws IOException {
        start(manager);
    }

    public HttpTaskServer() throws IOException {
        this.manager = new InMemoryTaskManager();

    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    public static void start(TaskManager manager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        Gson gson = getGson();
        server.createContext("/tasks", new HandlerTask(manager, gson));
        server.createContext("/subtasks", new HandlerSubTask(manager, gson));
        server.createContext("/epics", new HandlerEpic(manager, gson));
        server.createContext("/epics/subtasks", new HandlerEpic(manager, gson));
        server.createContext("/history", new HandlerHistory(manager, gson));
        server.createContext("/prioritized", new HandlerPriorTasks(manager, gson));
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                ;
        return gsonBuilder.create();
    }

    public TaskManager getManager() {
        return manager;
    }

    public static void stop(int delay) {
        server.stop(delay);
        System.out.println("HTTP Task Server остановлен");
    }
}
