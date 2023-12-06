package Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import Module.Task;
import Module.Epic;
import Module.SubTask;
public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics =  new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int nextId = 1;


    public int create(Task task) {
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public int create(Epic epic) {
        epic.setId(nextId);
        nextId++;
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public int create(SubTask subTask) {
        subTask.setId(nextId);
        nextId++;
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        epic.setSubTaskId(subTask.getId());
        updateStatus(epic);
        return subTask.getId();
    }

    public Collection<Task> getAllTasks(){
        Collection<Task> allTasks = tasks.values();
        return allTasks;
    }

    public Collection<Epic> getAllEpics(){
        Collection<Epic> allEpics = epics.values();
        return allEpics;
    }

    public Collection<SubTask> getAllSubTasks(){
        Collection<SubTask> allSubTasks = subTasks.values();
        return allSubTasks;
    }

    public void clearTask(){
        tasks.clear();
    }

    public void clearEpics(){
        epics.clear();
        subTasks.clear();
    }

    public void clearSubTask(){
        subTasks.clear();
        if (!epics.isEmpty()){
            for (Epic epic : epics.values()){
                epic.clearSubTaskId();
                updateStatus(epic);
            }
        }
    }

    public Task getTask(Integer id){
        if (!tasks.isEmpty()){
            for (Integer index : tasks.keySet()){
                if (index.equals(id)){
                    return tasks.get(id);
                }
            }
        }
        return null;
    }

    public Epic getEpic(Integer id){
        if (!epics.isEmpty()){
            for (Integer index : epics.keySet()){
                if (index.equals(id)){
                    return epics.get(id);
                }
            }
        }
        return null;
    }

    public SubTask getSubTask(Integer id){
        if (!subTasks.isEmpty()){
            for (Integer index : subTasks.keySet()){
                if (index.equals(id)){
                    return subTasks.get(id);
                }
            }
        }
        return null;
    }

    public void update(Task task){
        tasks.put(task.getId(), task);
    }

    public void update(Epic epic){
        epics.put(epic.getId(), epic);
    }

    public void update(SubTask subTask){
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpic(subTask.getEpicId());
        updateStatus(epic);
    }

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

    public void clearByIdTask(Integer id){
        if(!tasks.isEmpty() && tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

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

    public void clearByIdSubTask(Integer id){
        if (!subTasks.isEmpty() && subTasks.containsKey(id)){
            SubTask subTask = subTasks.get(id);
            Epic epic = getEpic(subTask.getEpicId());

            subTasks.remove(id);
            epic.deleteSubTaskId(id);
            updateStatus(epic);
        }
    }

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

    public ArrayList<Integer> getAllSubTaskId(Epic epic){
        return epic.getSubTaskId();
    }
}
