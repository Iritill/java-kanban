package Service.HttpTest;

import Service.Interface.TaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Service.InMemoryManager.*;
import Service.HttpTaskManager.*;
import Tasks.Task;
import Tasks.Epic;
import Tasks.SubTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestHttp {
    private HttpTaskServer server;
    private Task task;
    private Epic epic;
    private int epicId;
    private SubTask subtask;
    private Gson gson;
    private HttpClient client;
    private TaskManager manager;

    @BeforeEach
    void init() throws IOException {
        client = HttpClient.newHttpClient();
        manager = new InMemoryTaskManager();
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter());
        gsonBuilder.serializeNulls();
        gson = gsonBuilder.create();
        task = new Task("Задание 2", "Описание 2", LocalDateTime.now(), Duration.ofMinutes(1));
        manager.createTask(task);
        epic = new Epic("Большое задание 1", "Описание 1");
        epicId = manager.createEpic(epic);
        subtask = new SubTask("Подзадача 111", "Описание11", LocalDateTime.now().minusMinutes(500), 60L, epicId);
        manager.createSubTask(subtask);
        server = new HttpTaskServer(manager);
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    @Test
    void getTaskTest() throws IOException, InterruptedException {
        Task task2 = new Task("Задание 111111", "Описание 111111");
        manager.createTask(task2);

        //Проверка вывода одного таска
        URI url2 = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response2.statusCode());
        Task expected = gson.fromJson(response2.body(), Task.class);
        Assertions.assertNotNull(expected);

        //Проверка вывода всех тасков
        URI url3 = URI.create("http://localhost:8080/tasks");
        HttpRequest request3 = HttpRequest.newBuilder().uri(url3).GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response3.statusCode());
        ArrayList<Task> expected3 = gson.fromJson(response3.body(), ArrayList.class);
        Assertions.assertNotNull(expected3);

    }

    @Test
    void getSubTaskTest() throws IOException, InterruptedException {
        //Проверка вывода всех тасков
        URI url4 = URI.create("http://localhost:8080/subtasks");
        HttpRequest request4 = HttpRequest.newBuilder().uri(url4).GET().build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response4.statusCode());
        List<SubTask> expected4 = gson.fromJson(response4.body(), ArrayList.class);
        Assertions.assertNotNull(expected4);


        //Проверка вывода одного таска
        URI url5 = URI.create("http://localhost:8080/subtasks?id=3");
        HttpRequest request5 = HttpRequest.newBuilder().uri(url5).GET().build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response5.statusCode());
        SubTask expected5 = gson.fromJson(response5.body(), SubTask.class);
        Assertions.assertNotNull(expected5);

    }

    @Test
    void getEpicTest() throws IOException, InterruptedException {
        URI url5 = URI.create("http://localhost:8080/epics");
        HttpRequest request5 = HttpRequest.newBuilder().uri(url5).GET().build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response5.statusCode());
        List<Epic> expected5 = gson.fromJson(response5.body(), ArrayList.class);
        Assertions.assertNotNull(expected5);

        URI url6 = URI.create("http://localhost:8080/epics?id=2");
        HttpRequest request6 = HttpRequest.newBuilder().uri(url6).GET().build();
        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response6.statusCode());
        Epic expected6 = gson.fromJson(response6.body(), Epic.class);
        Assertions.assertNotNull(expected5);
    }

    @Test
    void getHistoryAndPriorTest() throws IOException, InterruptedException {
        URI url5 = URI.create("http://localhost:8080/prioritized");
        HttpRequest request5 = HttpRequest.newBuilder().uri(url5).GET().build();
        HttpResponse<String> response5 = client.send(request5, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response5.statusCode());
        List<Task> expected5 = gson.fromJson(response5.body(), List.class);
        Assertions.assertNotNull(expected5);

        URI url6 = URI.create("http://localhost:8080/history");
        HttpRequest request6 = HttpRequest.newBuilder().uri(url6).GET().build();
        HttpResponse<String> response6 = client.send(request6, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response6.statusCode());
        List<Task> expected6 = gson.fromJson(response6.body(), List.class);
        Assertions.assertNotNull(expected6);
    }

    @Test
    void newSubtaskTest() throws IOException, InterruptedException {
        LocalDateTime subtask21Time = LocalDateTime.now().minusYears(1000);
        SubTask subtask11 = new SubTask("Подзадача", "Описание",LocalDateTime.now().minusMinutes(800), 60L, epicId);
        URI url = URI.create("http://localhost:8080/subtasks");
        String json = gson.toJson(subtask11);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
        Task expected = (Task) gson.fromJson(response.body(), SubTask.class);
        Assertions.assertNotNull(expected);
        HttpTaskServer var10000 = this.server;
        SubTask actual = manager.getSubTask(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void newTaskTest() throws IOException, InterruptedException {
        LocalDateTime timeOfTask1 = LocalDateTime.now();
        Task task2 = new Task("Задание", "Описание", LocalDateTime.now().minusMinutes(900), 60L);
        URI url2 = URI.create("http://localhost:8080/tasks");
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        Task expected = gson.fromJson(response2.body(), Task.class);
        Assertions.assertNotNull(expected);
        Task actual = manager.getTask(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void newEpicTest() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Задание 2", "Описание 2");
        URI url2 = URI.create("http://localhost:8080/epics");
        String json2 = gson.toJson(epic2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response2.statusCode());
        Epic expected = gson.fromJson((String) response2.body(), Epic.class);
        Assertions.assertNotNull(expected);
        HttpTaskServer var10000 = this.server;
        Epic actual = manager.getEpic(expected.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateTaskTest() throws IOException, InterruptedException {
        LocalDateTime timeOfTask1 = LocalDateTime.now();
        Task task2 = new Task("Задание обновленное", "Описание", "DONE", LocalDateTime.now().minusMinutes(900), 60L, 1);
        URI url2 = URI.create("http://localhost:8080/tasks");
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, (long) response2.statusCode());
        HttpTaskServer var10000 = this.server;
        Task actual = manager.getTask(task2.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(task.getName(), actual.getName(), "Task не обновляется");
    }

    @Test
    void updateSubTaskTest() throws IOException, InterruptedException {
        LocalDateTime timeOfTask1 = LocalDateTime.now();
        SubTask task2 = new SubTask("Подзадача обновленная", "Описание", "DONE", LocalDateTime.now().minusMinutes(900), 60L, epicId, 3);
        URI url2 = URI.create("http://localhost:8080/subtasks");
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, (long) response2.statusCode());
        HttpTaskServer var10000 = this.server;
        SubTask actual = manager.getSubTask(task2.getId());
        Assertions.assertNotNull(actual);
        Assertions.assertNotEquals(subtask.getName(), actual.getName());
    }

    @Test
    void deleteTaskTest() throws IOException, InterruptedException {
        URI url2 = URI.create("http://localhost:8080/tasks?id=1");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, (long) response2.statusCode());
        HttpTaskServer var10000 = this.server;
        Assertions.assertEquals(new ArrayList<Task>(), manager.getAllTasks(), "Task не удалился");
    }

    @Test
    void deleteSubTaskTest() throws IOException, InterruptedException {
        URI url2 = URI.create("http://localhost:8080/subtasks?id=3");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, (long) response2.statusCode());
        HttpTaskServer var10000 = this.server;
        Assertions.assertEquals(new ArrayList<SubTask>(), manager.getAllSubTasks(), "SubTask не удалился");
    }

    @Test
    void deleteEpicTest() throws IOException, InterruptedException {
        URI url2 = URI.create("http://localhost:8080/epics?id=2");
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, (long) response2.statusCode());
        HttpTaskServer var10000 = this.server;
        Assertions.assertEquals(new ArrayList<Epic>(), manager.getAllEpics(), "Epic не удалился");
    }

    @Test
    void exceptionTest() throws IOException, InterruptedException {
        Task task2 = new Task("Задание", "Описание", LocalDateTime.now(), 60L);
        URI url2 = URI.create("http://localhost:8080/tasks");
        String json2 = gson.toJson(task2);
        HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);
        HttpRequest request2 = HttpRequest.newBuilder().uri(url2).POST(body2).build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(406, response2.statusCode(), "Выводится не тот статус код");
    }


}