import Module.*;
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
        manager.update(new Task(manager.getTask(task1).getName(), manager.getTask(task1).getDescription(), "DONE", task1));
        manager.update(new Task(manager.getTask(task2).getName(), manager.getTask(task2).getDescription(), "IN_PROGRESS", task2));


        //Обновление статуса SubTask
        manager.update(new SubTask(manager.getSubTask(sub1).getName(), manager.getSubTask(sub1).getDescription(), "DONE", sub1,  manager.getEpic(epic1)));
        manager.update(new SubTask(manager.getSubTask(sub2).getName(), manager.getSubTask(sub2).getDescription(), "IN_PROGRESS", sub2, manager.getEpic(epic1)));
        manager.update(new SubTask(manager.getSubTask(sub3).getName(), manager.getSubTask(sub3).getDescription(), "DONE", sub3, manager.getEpic(epic2)));

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

