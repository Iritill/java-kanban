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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
        assertEquals(new HashMap<>().keySet(), epicTest.getSubTaskId(), "При удалении сабтаска остается id в эпике");
        assertNull(subTask, "subTask все еще существует и хранит в себе какие-то значения");
    }

    @Test
    void dataTestForEpicAndSubTask(){

        int task4 = taskManager.createEpic(new Epic("Епик номер 1", "312313"));

        assertNull(taskManager.getEpic(task4).getEndDate(), "EndDate не равен null, что невозможно, тк у Epic нет SubTasks");
        assertNull(taskManager.getEpic(task4).getStartDate(), "StartDate не равен null, что невозможно, тк у Epic нет SubTasks");
        assertNull(taskManager.getEpic(task4).getDuration(), "Duration не равен null, что невозможно, тк у Epic нет SubTasks");

        int task5 = taskManager.createSubTask(new SubTask("Сабтаск номер1", "312313", LocalDateTime.now().minusMinutes(500), 50L, task4));
        int task6 = taskManager.createSubTask(new SubTask("Сабтаск номер2", "312313123123", LocalDateTime.now().minusMinutes(450), 450L, task4));

        assertNotNull(taskManager.getEpic(task4).getEndDate(), "EndDate не обновляется");
        assertNotNull(taskManager.getEpic(task4).getStartDate(), "StartDate не обновляется");
        assertNotNull(taskManager.getEpic(task4).getDuration(), "Duration не обновляется");

        assertEquals(taskManager.getSubTask(task6).getEndDate(), taskManager.getEpic(task4).getEndDate(), "EndDate неправильно обновляется при добавлении сабтасков");

        assertEquals(taskManager.getSubTask(task5).getStartDate(), taskManager.getEpic(task4).getStartDate(), "StartDate неправильно обновляется при добавлении сабтасков");

        assertEquals(taskManager.getSubTask(task5).getDuration().plusMinutes(taskManager.getSubTask(task6).getDuration().toMinutes()), taskManager.getEpic(task4).getDuration(), "Duration неправильно обновляется при добавлении сабтасков");

    }

    @Test
    void dataTestForTask(){

        int task4 = taskManager.createTask(new Task("Task номер 1", "312313"));

        assertNull(taskManager.getTask(task4).getEndDate(), "EndDate не равен null, что невозможно, тк у Task нет SubTasks");
        assertNull(taskManager.getTask(task4).getStartDate(), "StartDate не равен null, что невозможно, тк у Task нет SubTasks");
        assertNull(taskManager.getTask(task4).getDuration(), "Duration не равен null, что невозможно, тк у Task нет SubTasks");

        int task5 = taskManager.createTask(new Task("Task номер 2", "312313", LocalDateTime.now().minusMinutes(500), 50L));
        int task6 = taskManager.createTask(new Task("Task номер 3", "312313123123", LocalDateTime.now().minusMinutes(450), 450L));

        assertNotNull(taskManager.getTask(task5).getEndDate(), "EndDate не обновляется при создании Task");
        assertNotNull(taskManager.getTask(task6).getStartDate(), "StartDate не обновляется при создании Task");
        assertNotNull(taskManager.getTask(task5).getDuration(), "Duration не обновляется при создании Task");

        assertThrows(RuntimeException.class, () -> {
            int task8 = taskManager.createTask(new Task("Task номер 8", "312313", LocalDateTime.now().minusMinutes(480), 100L));
        }, "Добавление такого Task должно приводить в исключению из-за пересечения");

        assertDoesNotThrow(() -> {
            int task9 = taskManager.createTask(new Task("Task номер 8", "312313", LocalDateTime.now(), 100L));
        }, "Добавление такого Task не должно приводить к исключению");
    }

}