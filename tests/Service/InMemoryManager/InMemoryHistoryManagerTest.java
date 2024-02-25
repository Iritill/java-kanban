package Service.InMemoryManager;

import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void historyTest(){
        TaskManager taskManager = Manager.getDefault();

        int taskFirst = taskManager.create(new Task("Первый таск", "обычный"));
        int taskSecond = taskManager.create(new Task("Второй таск", "обычный"));

        int taskEpicFirst = taskManager.create(new Epic("Первый Epic", "Эпик"));

        int taskSubTaskFirst = taskManager.create(new SubTask("Первый сабтаск", "сабтаск", taskManager.getEpicForSubTask(taskEpicFirst)));

        ArrayList<Task> arrayForTaskTest = new ArrayList<>();

        arrayForTaskTest.add(taskManager.getTask(taskFirst));
        arrayForTaskTest.add(taskManager.getTask(taskSecond));
        arrayForTaskTest.add(taskManager.getSubTask(taskSubTaskFirst));
        arrayForTaskTest.add(taskManager.getEpic(taskEpicFirst));

        assertEquals(taskManager.getHistory().get(0), arrayForTaskTest.get(0), "Добавление первого элемента не работает исправно");
        assertEquals(taskManager.getHistory().get(2), arrayForTaskTest.get(2), "Добавление сабтаска не работает исправно");


        arrayForTaskTest.add(taskManager.getTask(taskSecond));
        assertEquals(taskManager.getHistory().size(), 4, "Удаление и перезаписывание работает корректно");

        arrayForTaskTest.add(taskManager.getTask(taskFirst));
        assertEquals(taskManager.getHistory().get(3), arrayForTaskTest.get(0), "Удаление и перезаписывание первого элемента работает корректно");

        arrayForTaskTest.add(taskManager.getTask(taskFirst));
        assertEquals(taskManager.getHistory().get(3), arrayForTaskTest.get(6), "Удаление и перезаписывание последнего элемента работает корректно");

        Task taskForUpdate = taskManager.getTaskForUpdate(taskFirst);
        taskForUpdate.setStatus("DONE");
        taskManager.update(taskForUpdate);




    }

}