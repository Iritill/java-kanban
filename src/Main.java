import Tasks.*;
import Service.Manager;


public class Main {
    public static void main(String[] args){
        Manager manager = new Manager();

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
                "Простите песенка хорошая в голове заиграла", manager.getEpic(epic1)));
        int sub2 = manager.create(new SubTask("Может быть устроиться на работу",
                "Как же я устал... Но работать надо", manager.getEpic(epic1)));

        //Cоздание второго Epic
        int epic2 = manager.create(new Epic("Заработать мало проблем, пока пытаюсь заработать много деняк",
                "Ну очень много деняк надо, но при этом поменьше проблем заработать"));

        //Создание SubTask для второго Epic
        int sub3 = manager.create(new SubTask("Отдохнуть",
                "Я уже столько багов нашел что у меня бошка взрывается, а то работать не смогу", manager.getEpic(epic2)));

        // Пункт 2

        //Вывод всех Tasks, Epics, SubTasks
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubTasks());

        // Пункт 3


        //Обновление статуса Task
        Task task1ToUpdate = manager.getTask(task1);
        task1ToUpdate.setStatus("DONE");
        manager.update(task1ToUpdate);
        Task task2ToUpdate = manager.getTask(task2);
        task2ToUpdate.setStatus("IN_PROGRESS");
        manager.update(task2ToUpdate);

        //Обновление статуса SubTask

        SubTask subTask1ToUpdate = manager.getSubTask(sub1);
        subTask1ToUpdate.setStatus("NEW");
        manager.update(subTask1ToUpdate);

        SubTask subTask2ToUpdate = manager.getSubTask(sub2);
        subTask2ToUpdate.setStatus("IN_PROGRESS");
        manager.update(subTask2ToUpdate);

        SubTask subTask3ToUpdate = manager.getSubTask(sub3);
        subTask3ToUpdate.setStatus("DONE");
        manager.update(subTask3ToUpdate);


        //Вывод и проверка обновления статуса Task
        System.out.println(manager.getTask(task1));
        System.out.println(manager.getTask(task2));

        //Вывод и проверка обновления статуса SubTask
        System.out.println(manager.getSubTask(sub1));
        System.out.println(manager.getSubTask(sub2));
        System.out.println(manager.getSubTask(sub3));

        //Вывод и проверка обновления статуса Epic
        System.out.println(manager.getEpic(epic1));
        System.out.println(manager.getEpic(epic2));

        //Пункт 4

        //Удаление Task1 и проверка
        manager.clearByIdTask(task1);
        System.out.println(manager.getAllTasks());

        //даление Epic2 и проверка на удаление самого Epic2 и SubTask которые относились к нему
        manager.clearByIdEpic(epic2);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        //Удаление SubTask и проверка на удаление самого SubTask из списка все SubTasks и из списка Epic к которому он относится
        manager.clearByIdSubTask(sub2);
        System.out.println(manager.getAllSubTasks());
        System.out.println(manager.getAllSubTaskByEpic(epic1));

    }
}

