package Service;

import Service.Interface.HistoryManager;
import Service.Interface.TaskManager;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ManagerTest {
    @Test
    public void positiveManager(){
        TaskManager taskManager = Manager.getDefault();
        int taskForTest = taskManager.create(new Task("Пиупиупиу", "Пиупиу"));
        Task taskForHistoryTest = taskManager.getTask(taskForTest);
        assertEquals(1, taskManager.getHistory().size(), "История не сохраняется и не работает");
        //Одним тестом мы покрыли весь Manager
    }

}