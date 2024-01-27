import Service.InMemoryManager.InMemoryTaskManager;
import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.*;


public class Main {
    public static void main(String[] args){
        TaskManager manager = Manager.getDefault();

        // Пункт 1

        //Создание обычных Task
        int task1 = manager.create(new Task("Сделать проект по 3 спринту",
                "Нужно прям очень сильно да очень сильно постараться"));
        int task2 = manager.create(new Task("Сделать проект по 4 спринту",
                "Мы уже опазываем тут очень грустно надо постараться"));

        //Создание первого Epic
        int epic1 = manager.create(new Epic("Заработать много деняк",
                "Ну очень много деняк надо. На покушать там, одеться красиво"));

        //Создание SubTasks для первого Epic
        int sub1 = manager.create(new SubTask("УАУАУАУАУАУАУА",
                "Простите песенка хорошая в голове заиграла", manager.getEpicForSubTask(epic1)));
        int sub2 = manager.create(new SubTask("Может быть устроиться на работу",
                "Как же я устал... Но работать надо", manager.getEpicForSubTask(epic1)));

        //Cоздание второго Epic
        int epic2 = manager.create(new Epic("Заработать мало проблем, пока пытаюсь заработать много деняк",
                "Ну очень много деняк надо, но при этом поменьше проблем заработать"));

        //Создание SubTask для второго Epic
        int sub3 = manager.create(new SubTask("Отдохнуть",
                "Я уже столько багов нашел что у меня бошка взрывается, а то работать не смогу", manager.getEpicForSubTask(epic2)));

        // Пункт 2

        //Вывод всех Tasks, Epics, SubTasks
        System.out.println(manager.getEpic(epic1));
        System.out.println(manager.getTask(task1));
        System.out.println(manager.getSubTask(sub1));
        System.out.println(manager.getHistory());

        Task taskForUpdate = manager.getTaskForUpdate(task1);
        taskForUpdate.setStatus("DONE");
        manager.update(taskForUpdate);

        System.out.println(manager.getTask(task1));
        System.out.println(manager.getHistory());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getHistory());
        SubTask subtaskForUpdate = manager.getSubTaskForUpdate(sub1);
        subtaskForUpdate.setStatus("IN_PROGRESS");
        manager.update(subtaskForUpdate);
        System.out.println(manager.getSubTask(sub1));
        System.out.println((manager.getEpic(epic1)));
        System.out.println(manager.getHistory());
        System.out.println(manager.getAllSubTaskByEpic(epic1));
        System.out.println(manager.getHistory());



        Task task11 = manager.getTask(task1);
        Task task22 = manager.getTask(task1);

        System.out.println(manager.getSubTaskForUpdate(sub2).getId());
    }
}

