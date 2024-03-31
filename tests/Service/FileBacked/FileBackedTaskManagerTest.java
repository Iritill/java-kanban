package Service.FileBacked;

import Service.FileBackedManager.Exception.ManagerFileNotExistsException;
import Service.FileBackedManager.Exception.TaskValidException;
import Service.FileBackedManager.FileBackedTaskManager;
import Service.Manager;
import Tasks.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest{
    @Test
    void loadFromFile() {
        File tempFile;

        try {
            tempFile = File.createTempFile("hello", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager taskManager1 = Manager.getDefaultFileBackedTaskManager(tempFile);
        Task task11 = new Task("Задание 111212", "Описание 1");
        taskManager1.createTask(task11);
        Task task22 = new Task("Задание 2", "Описание 2");
        taskManager1.createTask(task22);
        Epic epic = new Epic("Большое задание 1", "Описание 1");
        taskManager1.createEpic(epic);
        SubTask subtask11 = new SubTask("Подзадача 1", "Описание", epic);
        taskManager1.createSubTask(subtask11);
        SubTask subtask12 = new SubTask("Подзадача 2", "Описание", epic);
        taskManager1.createSubTask(subtask12);
        Epic epic2 = new Epic("Большое задание 2", "Описание 2");
        taskManager1.createEpic(epic2);
        SubTask subtask21 = new SubTask("Подзадача 1", "Описание)", epic2);
        taskManager1.createSubTask(subtask21);
        FileBackedTaskManager managerLoad = FileBackedTaskManager.loadFromManager(tempFile);
        assertNotNull(managerLoad);
        assertEquals(task11, managerLoad.getTask(1));
        Task taskForUpdate = new Task("Задание 111212", "Описание 1", "DONE", task11.getId());
        managerLoad.update(taskForUpdate);
        assertEquals(taskForUpdate, managerLoad.getTask(1));
        assertEquals(epic, managerLoad.getEpic(3));
        assertEquals(subtask11, managerLoad.getSubTask(4));
        assertNotNull(managerLoad.getAllTasks());
        assertFalse(managerLoad.getAllTasks().isEmpty());
        assertNotEquals(managerLoad.getAllTasks(), taskManager1.getAllTasks());
        assertNotNull(managerLoad.getAllSubTasks());
        assertFalse(managerLoad.getAllSubTasks().isEmpty());
        assertEquals(managerLoad.getAllSubTasks(), taskManager1.getAllSubTasks());
        assertNotNull(managerLoad.getAllEpics());
        assertFalse(managerLoad.getAllEpics().isEmpty());
        assertEquals(managerLoad.getAllEpics(), taskManager1.getAllEpics());
        assertEquals(managerLoad.getHistory(), taskManager1.getHistory());
        managerLoad.clearSubTask();
        assertEquals(new ArrayList<SubTask>(), managerLoad.getAllSubTasks());

        File file = new File("fasfasf");
        try{
            FileBackedTaskManager taskManager3 = new FileBackedTaskManager(file);
        } catch (ManagerFileNotExistsException e){
            assertEquals("Файла не существует", e.getMessage());
        }
    }

    @Test
    void loadFromFileForDate() {
        File tempFile;

        try {
            tempFile = File.createTempFile("hello", ".tmp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager taskManager1 = Manager.getDefaultFileBackedTaskManager(tempFile);
        Task task11 = new Task("Задание 111212", "Описание 1", LocalDateTime.now(), 100L);
        taskManager1.createTask(task11);


        FileBackedTaskManager managerLoad = FileBackedTaskManager.loadFromManager(tempFile);
        assertNotNull(managerLoad);
        assertEquals(task11, managerLoad.getTask(1), "Создаваемый таск не равен таску, загруженному из файла");

        try{
            Task task12 = new Task("Задание для проверки пересечения", "Описание 1", LocalDateTime.now(), 100L);
        } catch (TaskValidException e){
            assertEquals("Задача пересекается", e.getMessage());
        }


        File file = new File("fasfasf");
        try{
            FileBackedTaskManager taskManager3 = new FileBackedTaskManager(file);
        } catch (ManagerFileNotExistsException e){
            assertEquals("Файла не существует", e.getMessage());
        }
    }
}