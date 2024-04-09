package Service.Interface;

import Tasks.Epic;
import Tasks.SubTask;
import Tasks.Task;

import java.util.List;

public interface TaskManager {
    public List<Task> getPrioritizedTasks();
    public int createTask(Task task);
    public int createEpic(Epic epic);
    public int createSubTask(SubTask subTask);
    public SubTask getSubTaskForUpdate(Integer id);
    public Task getTaskForUpdate(Integer id);
    public List<Task> getAllTasks();
    public List<Epic> getAllEpics();
    public List<SubTask> getAllSubTasks();
    public void clearTask();
    public void clearEpics();
    public void clearSubTask();
    public Task getTask(Integer id);
    public Epic getEpic(Integer id);
    public SubTask getSubTask(Integer id);
    public void update(Task task);
    public Epic getEpicForSubTask(Integer id);

    public void update(Epic epic);
    public void update(SubTask subTask);
    public void updateStatus(Epic epic);
    public void clearByIdTask(Integer id);
    public void clearByIdEpic(Integer id);
    public void clearByIdSubTask(Integer id);
    public List<SubTask> getAllSubTaskByEpic(Integer id);
    public List<Task> getHistory();

    public void setStartTimeEpic(Epic epic);
    public void setEndTimeEpic(Epic epic);
    public void setDurationEpic(Epic epic);

}
