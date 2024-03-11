package Service.FileBackedManager;

import Service.FileBackedManager.Exception.ManagerFileNotExistsException;
import Service.FileBackedManager.Exception.ManagerReadException;
import Service.FileBackedManager.Exception.ManagerSaveException;
import Service.InMemoryManager.*;
import Tasks.*;
import java.io.*;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file){
        super();
        this.file = file;

        if(!file.exists()){
            throw new ManagerFileNotExistsException("Файла не существует");
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(CSVFormat.getHead());
            writer.newLine();

            for (Task task : tasks.values()) {
                writer.write(CSVFormat.toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {
                writer.write(CSVFormat.toString(epic));
                writer.newLine();
            }
            for (SubTask subtask : subTasks.values()) {
                writer.write(CSVFormat.toString(subtask));
                writer.newLine();
            }
            writer.newLine();
            writer.write(CSVFormat.historyToString(historyManager));
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Не получается сохранить файл:" + file.getName(), e);
        }

    }

    public static FileBackedTaskManager loadFromManager(File file) {
        HashMap<Integer, Task> all = new HashMap<>();
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Task task = null;
            if (reader.readLine() == null) {
                System.out.println("");
                return fileManager;
            }
            while (reader.ready()) {
                String result = reader.readLine();
                if (result.isEmpty()) {
                    break;
                }
                task = CSVFormat.fromString(result);
                if (task.getType() == TasksType.TASK) {
                    fileManager.tasks.put(task.getId(), task);
                    all.put(task.getId(), task);
                } else if (task.getType() == TasksType.SUBTASK) {

                    SubTask subtask = (SubTask) task;
                    fileManager.subTasks.put(subtask.getId(), subtask);
                    all.put(subtask.getId(), subtask);
                } else if (task.getType() == TasksType.EPIC) {
                    Epic epic = (Epic) task;
                    fileManager.epics.put(epic.getId(), epic);
                    all.put(epic.getId(), epic);

                }
            }

            String history = reader.readLine();
            for (Integer inter : CSVFormat.historyFromString(history)) {
                historyManager.add(all.get(inter));
            }
            return fileManager;

        } catch (IOException e) {
            throw new ManagerReadException("Не получается прочитать файл:" + file.getName(), e);
        }
    }


    @Override
    public SubTask getSubTask(Integer id) {
        SubTask task = super.getSubTask(id);;
        save();
        return task;
    }

    @Override
    public void updateStatus(Epic epic) {
        super.updateStatus(epic);
        save();
    }

    @Override
    public void update(SubTask subTask) {
        super.update(subTask);
        save();
    }

    @Override
    public void clearByIdTask(Integer id) {
        super.clearByIdTask(id);
        save();
    }

    @Override
    public void clearByIdSubTask(Integer id) {
        super.clearByIdSubTask(id);
        save();
    }

    @Override
    public void clearByIdEpic(Integer id) {
        super.clearByIdEpic(id);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }


    @Override
    public Epic getEpic(Integer id) {
        Epic task = super.getEpic(id);;
        save();
        return task;
    }


    @Override
    public Task getTask(Integer id) {
        Task task = super.getTask(id);;
        save();
        return task;
    }

    @Override
    public void clearSubTask() {
        super.clearSubTask();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }


    @Override
    public int createEpic(Epic epic) {
        int returnTask = super.createEpic(epic);
        save();
        return returnTask;
    }


    @Override
    public int createSubTask(SubTask subTask) {
        int returnTask = super.createSubTask(subTask);
        save();
        return returnTask;
    }

    @Override
    public int createTask(Task task) {
        int returnTask = super.createTask(task);
        save();
        return returnTask;
    }

}
