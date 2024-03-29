package Service.InMemoryManager;

import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthUI;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    protected TaskManager taskManager = Manager.getDefault();
    @BeforeEach
    void beforeEach(){
        int taskForTestCreate1 = taskManager.createTask(new Task("Первый таск", "Обычный"));
        int taskForTestCreate2 = taskManager.createTask(new Task("Второй таск", "Обычный"));

        int taskForTestCreate3 = taskManager.createEpic(new Epic("Третий таск", "Эпик"));

        int taskForTestCreate4 = taskManager.createSubTask(new SubTask("Четвертый таск", "Сабтаск", taskManager.getEpicForSubTask(taskForTestCreate3)));
        int taskForTestCreate5 = taskManager.createSubTask(new SubTask("Пятый таск", "Сабтаск", taskManager.getEpicForSubTask(taskForTestCreate3)));
    }

    @Test
    public void getAllTasksTest(){
        assertNotNull(taskManager.getAllTasks(), "Не выводятся все обычные таски");
        assertNotNull(taskManager.getAllEpics(), "Не выводятся все эпические таски");
        assertNotNull(taskManager.getAllSubTasks(), "Не выводятся все саб таски");
    }

    @Test
    public void updateTaskTest(){
        Task  taskForTest = taskManager.getTaskForUpdate(1);
        Task taskForTestNotUpdate = new Task(taskForTest.getName(), taskForTest.getDescription());
        taskForTest.setStatus("DONE");
        taskManager.update(taskForTest);
        assertNotEquals(taskForTest.getStatus(), taskForTestNotUpdate.getStatus(), "Статус не изменился");
    }

    @Test
    public void updateEpicTest(){
        Epic  taskForTest = taskManager.getEpicForSubTask(3);
        Epic taskForTestNotUpdate = new Epic(taskForTest.getName(), taskForTest.getDescription());
        taskForTest.setDescription("Поменяли");
        taskManager.update(taskForTest);
        assertNotEquals(taskForTest.getDescription(), taskForTestNotUpdate.getDescription(), "Описание не изменилось");
    }

    @Test
    public void updateSubTaskTest(){
        SubTask  taskForTest = taskManager.getSubTaskForUpdate(4);
        SubTask taskForTestNotUpdate = new SubTask(taskForTest.getName(), taskForTest.getDescription(), taskManager.getEpicForSubTask(taskForTest.getEpicId()));
        taskForTest.setDescription("Поменяли");
        taskManager.update(taskForTest);
        assertNotEquals(taskForTest.getDescription(), taskForTestNotUpdate.getDescription(), "Описание не изменилось");
    }

    @Test
    public void clearTasksText(){
        taskManager.clearByIdSubTask(4);
        assertEquals(1, taskManager.getAllSubTasks().size(), "Не удалился саб таск");
        taskManager.clearSubTask();
        assertEquals(0, taskManager.getAllSubTasks().size(), "Не удалились все саб таски");

        taskManager.clearByIdTask(1);
        assertEquals(1, taskManager.getAllTasks().size(), "Не удалился таск");
        taskManager.clearTask();
        assertEquals(0, taskManager.getAllTasks().size(), "Не удалились все таски");

        taskManager.clearByIdEpic(3);
        assertEquals(0, taskManager.getAllEpics().size(), "Не удалился эпик таск");

        int EpicForTest = taskManager.createEpic(new Epic("Эпик для теста удаления", "Эпик"));

        taskManager.clearEpics();
        assertEquals(0, taskManager.getAllEpics().size(), "Не удалились все эпик таски");



    }

    @Test
    void getAllSubTaskForEpicTest(){
        ArrayList<SubTask> forTest = new ArrayList<>();
        forTest.add(taskManager.getSubTaskForUpdate(4));
        forTest.add(taskManager.getSubTaskForUpdate(5));
        assertEquals(forTest, taskManager.getAllSubTaskByEpic(3), "Не выводит все саб таски");

    }

    @Test
    void createSubTaskTest(){
        Epic epicTest = new Epic("Пиу пиу пиу", "пиу пиу");

        int subTaskTest = taskManager.createSubTask(new SubTask("Сабтаск", "саб таск", epicTest));

        assertEquals(0, subTaskTest, "Нельзя задать саб таск с эпиком, чей id не был сгенерирован при помощи taskManager");
    }

    @Test
    void setNameForTaskTest(){
        Task taskForTest = new Task("Не работает", "Описание");
        taskForTest.setName("Работает");
        assertEquals("Работает", taskForTest.getName(), "Имя не меняется");
    }

    @Test
    void removeSubTask(){
        Epic epicTest = new Epic("Пиу пиу пиу", "пиу пиу");
        int subTaskTest = taskManager.createSubTask(new SubTask("Сабтаск", "саб таск", epicTest));
        SubTask subTask = taskManager.getSubTaskForUpdate(subTaskTest);
        taskManager.clearByIdSubTask(subTaskTest);
        assertEquals(new ArrayList<>(), epicTest.getSubTaskId(), "При удалении сабтаска остается id в эпике");
        assertNull(subTask, "subTask все еще существует и хранит в себе какие-то значения");
    }

}