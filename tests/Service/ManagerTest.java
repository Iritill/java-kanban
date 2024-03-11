package Service;

import Service.InMemoryManager.InMemoryTaskManager;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ManagerTest {
    @Test
    public void positiveManager(){
        InMemoryTaskManager taskManager2 = new InMemoryTaskManager();
        int taskForTest = taskManager2.createTask(new Task("Пиупиупиу", "Пиупиу"));
        Task taskForHistoryTest = taskManager2.getTask(taskForTest);
        System.out.println(taskManager2.getHistory());
        assertEquals(1, taskManager2.getHistory().size(), "История не сохраняется и не работает");
    }

}