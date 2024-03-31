package Service.InMemoryManager;
import java.time.LocalDateTime;
import java.util.*;

import Service.FileBackedManager.Exception.TaskValidException;
import Service.Interface.*;
import Service.Manager;
import Tasks.Task;
import Tasks.Epic;
import Tasks.SubTask;
import java.util.stream.Collectors;
public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics =  new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<Task>(new ComparatorTask());

    protected static HistoryManager historyManager = null;

    public InMemoryTaskManager(){
        historyManager = Manager.getDefaultHistory();
    }
    private int nextId = 1;

    private void validate(Task task) {
        LocalDateTime startDate = task.getStartDate();
        LocalDateTime endDate = task.getEndDate();
        if (startDate != null) {
            for (Task taskValidated2 : prioritizedTasks) {
                if (taskValidated2.getStartDate() == null) {
                    continue;
                }
                if (taskValidated2.getId() == task.getId()) {
                    continue;
                }

                if (startDate.isBefore(taskValidated2.getEndDate()) && taskValidated2.getStartDate().isBefore(endDate)) {
                    throw new TaskValidException("Задача пересекается");
                }
            }
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        List<Task> list = new ArrayList<Task>(prioritizedTasks);
        return list;
    }
    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }


    @Override
    public int createTask(Task task) {
        validate(task);
        task.setId(nextId);
        nextId++;
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task.getId();
    }
    @Override
    public int createEpic(Epic epic) {
        validate(epic);
        epic.setId(nextId);
        nextId++;
        epic.setStatus("NEW");
        epics.put(epic.getId(), epic);
        prioritizedTasks.add(epic);
        return epic.getId();
    }
    @Override
    public int createSubTask(SubTask subTask) {
        validate(subTask);
        if(epics.containsKey(subTask.getEpicId())) {
            subTask.setId(nextId);
            nextId++;
            subTasks.put(subTask.getId(), subTask);
            prioritizedTasks.add(subTask);
            Epic epic = getEpicForSubTask(subTask.getEpicId());
            epic.setSubTaskId(subTask.getId());
            epic.setSubTaskIdForTree(subTask);
            epic.setDuration();
            epic.setStartDate();
            epic.setEndDate();
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
//    @Override
//    public void update(Task task){
//        int id = task.getId();
//
//        if (!tasks.isEmpty()){
//            for (Integer index : tasks.keySet()){
//                if (index.equals(id)){
//                    tasks.put(task.getId(), task);
//
//                }
//            }
//        }
//        return;
//    }

    @Override
    public void update(Task task){
        int id = task.getId();
        if (!tasks.isEmpty()){
            tasks.keySet().stream()
                    .filter(index -> index.equals(id))
                    .forEach(index -> tasks.put(task.getId(), task));
        }

    }
//    @Override
//    public void update(Epic epic){
//        int id = epic.getId();
//
//        if (!epics.isEmpty()){
//            for (Integer index : epics.keySet()){
//                if (index.equals(id)){
//                    epics.put(epic.getId(), epic);
//                }
//            }
//        }
//        return;
//    }

    @Override
    public void update(Epic epic){
        int id = epic.getId();

        if (!epics.isEmpty()){
            epics.keySet().stream()
                    .filter(index -> index.equals(id))
                    .forEach(index -> epics.put(epic.getId(), epic));
        }
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
                        updateStatus(getEpicForSubTask(subTask.getEpicId()));
                    }
                }
            }
        }
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

    public HistoryManager getHistoryManager(){
        return historyManager;
    }
    @Override
    public ArrayList<Integer> getAllSubTaskId(Epic epic){
        return epic.getSubTaskId();
    }
    class ComparatorTask implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.equals(task2)) {
                return 0;
            } else if (task1.getStartDate() == null) {
                return 1;
            } else if (task2.getStartDate() == null) {
                return -1;
            } else if (task1.getStartDate().isBefore(task2.getStartDate())) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
