import Service.FileBackedManager.CSVFormat;
import Service.FileBackedManager.FileBackedTaskManager;
import Service.HttpTaskManager.HttpTaskServer;
import Service.InMemoryManager.*;
import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){
        File file = new File(".\\src\\Resources\\file.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        int task1 = manager.createTask(new Task("Один", "312313", LocalDateTime.now().plusMinutes(50), 60L));
        Task tasks = manager.getTask(task1);
        int task2 = manager.createTask(new Task("Два", "312313123123", LocalDateTime.now().plusMinutes(410), 50L));
        int task3 = manager.createTask(new Task("Три", "312313123", LocalDateTime.now().plusMinutes(460), 85L));
        Task tasks2 = manager.getTask(task2);
        Task tasks3 = manager.getTask(task1);
        int task4 = manager.createEpic(new Epic("Епик номер 1", "312313"));
        int task5 = manager.createSubTask(new SubTask("Сабтаск номер1", "312313", LocalDateTime.now().minusMinutes(500), 50L, task4));
        Task tasks4 = manager.getEpic(task4);
        Task tasks5 = manager.getSubTask(task5);
        int task6 = manager.createSubTask(new SubTask("Сабтаск номер2", "312313123123", LocalDateTime.now().minusMinutes(450), 450L, task4));
        Task tasks6 = manager.getSubTask(task6);

        System.out.println(manager.getHistory());
        System.out.println(manager.getPrioritizedTasks());

        GsonBuilder gsonBuilder = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter());
        Gson gson = gsonBuilder.create();

        System.out.println(gson.toJson(tasks));
//
//        File file = new File(".\\src\\Resources\\file.txt");
//        FileBackedTaskManager manager = FileBackedTaskManager.loadFromManager(file);
//
//        int task4 = manager.createTask(new Task("++((&*&))", "корнеплод"));
//        Task tasks4 = manager.getTask(task4);
//
//        int task5 = manager.createEpic(new Epic("Парарарарарывпшывп", "())У!№"));
//        Task tasks5 = manager.getEpic(task5);
//
//        Task tasks8 = manager.getTask(task4);
//
//        System.out.println(manager.getAllTasks());

        TaskManager manager1 = new InMemoryTaskManager();

        int taskFirst = manager1.createTask(new Task("Первый таск", "обычный"));
        int taskSecond = manager1.createTask(new Task("Второй таск", "обычный"));

        Task task11 = manager1.getTask(taskFirst);
        Task task21 = manager1.getTask(taskSecond);
        Task task31 = manager1.getTask(taskFirst);

        System.out.println(manager1.getHistory());


    }
}


