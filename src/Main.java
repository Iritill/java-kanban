import Service.FileBackedManager.CSVFormat;
import Service.FileBackedManager.FileBackedTaskManager;
import Service.InMemoryManager.*;
import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args){
        CSVFormat format = new CSVFormat();
        File file = new File(".\\src\\Resources\\file.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(format, file);

        int task1 = manager.createTask(new Task("1313", "312313"));
        Task tasks = manager.getTask(task1);
        int task2 = manager.createTask(new Task("131331", "312313123123"));
        Task tasks2 = manager.getTask(task2);
        Task tasks3 = manager.getTask(task1);

        System.out.println(manager.getHistory());
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


