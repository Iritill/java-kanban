package Service.InMemoryManager;

import Service.Interface.HistoryManager;
import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void historyReplaceTest(){
        TaskManager taskManager = Manager.getDefault();

        int taskFirst = taskManager.createTask(new Task("Первый таск", "обычный"));
        int taskSecond = taskManager.createTask(new Task("Второй таск", "обычный"));

        int taskEpicFirst = taskManager.createEpic(new Epic("Первый Epic", "Эпик"));

        int taskSubTaskFirst = taskManager.createSubTask(new SubTask("Первый сабтаск", "сабтаск", taskManager.getEpicForSubTask(taskEpicFirst)));

        ArrayList<Task> arrayForTaskTest = new ArrayList<>();

        arrayForTaskTest.add(taskManager.getTask(taskFirst));
        arrayForTaskTest.add(taskManager.getTask(taskSecond));
        arrayForTaskTest.add(taskManager.getSubTask(taskSubTaskFirst));
        arrayForTaskTest.add(taskManager.getEpic(taskEpicFirst));

        System.out.println(taskManager.getHistory());

        assertEquals(taskManager.getHistory().get(0), arrayForTaskTest.get(0), "Добавление первого элемента не работает исправно");
        assertEquals(taskManager.getHistory().get(2), arrayForTaskTest.get(2), "Добавление сабтаска не работает исправно");

        arrayForTaskTest.add(taskManager.getTask(taskSecond));
        assertEquals(taskManager.getHistory().size(), 4, "Перезаписывание таска в истории работает не корректно");

        arrayForTaskTest.add(taskManager.getTask(taskFirst));
        assertEquals(taskManager.getHistory().get(3), arrayForTaskTest.get(0), "Перезаписывание первого элемента в истории работает не корректно");

        arrayForTaskTest.add(taskManager.getTask(taskFirst));
        assertEquals(taskManager.getHistory().get(3), arrayForTaskTest.get(6), "Перезаписывание последнего элемента в истории работает не корректно");
    }

    @Test
    public void historyDataTimeTest(){
        TaskManager taskManager = Manager.getDefault();

        int taskFirst = taskManager.createTask(new Task("Task номер 2", "312313", LocalDateTime.now().minusMinutes(500), 50L));
        int taskSecond = taskManager.createTask(new Task("Task номер 3", "312313123123", LocalDateTime.now().minusMinutes(450), 450L));


        int taskEpicFirst = taskManager.createEpic(new Epic("Первый Epic", "Эпик"));

        int taskSubTaskFirst = taskManager.createSubTask(new SubTask("Первый сабтаск", "сабтаск", LocalDateTime.now(), 100L, taskEpicFirst));

        Task task1 = taskManager.getTask(taskFirst);
        Task task2 = taskManager.getTask(taskSecond);
        Task task3 = taskManager.getEpic(taskEpicFirst);
        Task task4 = taskManager.getSubTask(taskSubTaskFirst);

        assertEquals(taskManager.getHistory().get(0).getEndDate(), task1.getEndDate(), "Добавление даты и времени элемента в историю не работает исправно для Task");
        assertEquals(taskManager.getHistory().get(2).getEndDate(), task3.getEndDate(), "Добавление даты и времени элемента в историю не работает исправно для SubTask");
        assertEquals(taskManager.getHistory().get(3).getEndDate(), task4.getEndDate(), "Добавление даты и времени элемента в историю не работает исправно для Epic");


    }

    @Test
    public void historyDeleteTest(){

        TaskManager taskManager = Manager.getDefault();

        ArrayList<Task> arrayForTest = new ArrayList<>();

        int taskFirst = taskManager.createTask(new Task("Первый таск", "обычный"));
        int taskSecond = taskManager.createTask(new Task("Второй таск", "обычный"));

        Task task1 = taskManager.getTask(taskFirst);
        Task task2 = taskManager.getTask(taskSecond);
        Task task3 = taskManager.getTask(taskFirst);

        arrayForTest.add(task1);
        arrayForTest.add(task2);

        //При вызове taskFirst во второй раз происходит удаление из истории с нулевого элемента и постановка в конец
        //В целом мне так и сказал наставник, что правильность выполнения работы методов remove and removeNode можно проверить только с помощью getHistory()
        assertNotEquals(arrayForTest.get(0), taskManager.getHistory().get(0), "Удаление работает не корректно");

    }


}