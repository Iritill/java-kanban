package Service.InMemoryManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import Service.Interface.*;
import Service.Manager;
import Tasks.Task;
import Tasks.Epic;
import Tasks.SubTask;
public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics =  new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private final HistoryManager historyManager = Manager.getDefaultHistory();
    private int nextId = 1;

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }


    @Override
    public int create(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }
    @Override
    public int create(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
        return epic.getId();
    }
    @Override
    public int create(SubTask subTask) {
        if(epics.containsKey(subTask.getEpicId())) {
            subTask.setId(nextId);
            nextId++;
            subTasks.put(subTask.getId(), subTask);
            Epic epic = getEpicForSubTask(subTask.getEpicId());
            epic.setSubTaskId(subTask.getId());
            updateStatus(epic);
            return subTask.getId();
        } else {
            return 0;
        }
    }
    @Override
    public List<Task> getAllTasks(){
        return new ArrayList<Task>(tasks.values());
    }
    @Override
    public List<Epic> getAllEpics(){
        return new ArrayList<Epic>(epics.values());
    }
    @Override
    public List<SubTask> getAllSubTasks(){
        return new ArrayList<SubTask>(subTasks.values());
    }

    @Override
    public void clearTask(){
        tasks.clear();
    }
    @Override
    public void clearEpics(){
        epics.clear();
        subTasks.clear();
    }
    @Override
    public void clearSubTask(){
        subTasks.clear();
        if (!epics.isEmpty()){
            for (Epic epic : epics.values()){
                epic.clearSubTaskId();
                updateStatus(epic);
            }
        }
    }
    @Override
    public Task getTask(Integer id){
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task getTaskForUpdate(Integer id){
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(Integer id){
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Epic getEpicForSubTask(Integer id){
        return epics.get(id);
    }
    @Override
    public SubTask getSubTask(Integer id){
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public SubTask getSubTaskForUpdate(Integer id){
        return subTasks.get(id);
    }
    @Override
    public void update(Task task){
        int id = task.getId();

        if (!tasks.isEmpty()){
            for (Integer index : tasks.keySet()){
                if (index.equals(id)){
                    tasks.put(task.getId(), task);

                }
            }
        }
        return;
    }
    @Override
    public void update(Epic epic){
        int id = epic.getId();

        if (!epics.isEmpty()){
            for (Integer index : epics.keySet()){
                if (index.equals(id)){
                    epics.put(epic.getId(), epic);
                }
            }
        }
        return;
    }
    @Override
    public void update(SubTask subTask){
        int id = subTask.getId();

        if (!subTasks.isEmpty()){
            if(epics.containsKey(subTask.getEpicId())) {
                for (Integer index : subTasks.keySet()) {
                    if (index.equals(id)) {
                        subTasks.put(subTask.getId(), subTask);
                        Epic epic = getEpicForSubTask(subTask.getEpicId());
                        updateStatus(epic);
                    }
                }
            }
        }
        return;
    }
    @Override
    public void updateStatus(Epic epic){
        if (epic.getSubTaskId().isEmpty()) {
            epic.setStatus("NEW");
        } else {
            boolean statusNew = false;
            boolean statusInProgress = false;
            boolean statusDone = false;

            for (Integer index : epic.getSubTaskId()){
                SubTask subTask = subTasks.get(index);
                if (subTask.getStatus().equals("NEW")){
                    statusNew = true;
                } else if (subTask.getStatus().equals("IN_PROGRESS")){
                    statusInProgress = true;
                } else if (subTask.getStatus().equals("DONE")){
                    statusDone = true;
                }
            }

            if (statusNew && !statusInProgress && !statusDone){
                epic.setStatus("NEW");
            } else if (!statusNew && !statusInProgress && statusDone){
                epic.setStatus("DONE");
            } else{
                epic.setStatus("IN_PROGRESS");
            }
        }
    }
    @Override
    public void clearByIdTask(Integer id){
        if(!tasks.isEmpty() && tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }
    @Override
    public void clearByIdEpic(Integer id){
        if (!epics.isEmpty() && epics.containsKey(id)) {
            ArrayList<Integer> subTaskId = epics.get(id).getSubTaskId();

            if (!subTaskId.isEmpty()) {
                for (Integer index : subTaskId){
                    subTasks.remove(index);
                }
                epics.remove(id);
                return;
            } else {
                epics.remove(id);
                return;
            }

        }
    }
    @Override
    public void clearByIdSubTask(Integer id){
        if (!subTasks.isEmpty() && subTasks.containsKey(id)){
            SubTask subTask = subTasks.get(id);
            Epic epic = getEpicForSubTask(subTask.getEpicId());

            subTasks.remove(id);
            epic.deleteSubTaskId(id);
            updateStatus(epic);
        }
    }
    @Override
    public ArrayList<SubTask> getAllSubTaskByEpic(Integer id){
        if (!subTasks.isEmpty() && !epics.isEmpty()){
            Epic epic = epics.get(id);
            ArrayList<SubTask> subTasksList = new ArrayList<>();

            for (Integer index : epic.getSubTaskId()){
                subTasksList.add(subTasks.get(index));
            }

            return subTasksList;
        }
        return null;
    }
    @Override
    public ArrayList<Integer> getAllSubTaskId(Epic epic){
        return epic.getSubTaskId();
    }
}
