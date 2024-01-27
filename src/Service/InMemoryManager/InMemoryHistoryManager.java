package Service.InMemoryManager;

import java.util.ArrayList;

import Service.Interface.HistoryManager;
import Tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> historyTask = new ArrayList<>();
    @Override
    public void add(Task task){
        if(historyTask.size() < 10){
            Task taskForArray = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getId());
            historyTask.add(taskForArray);
        }
        else {
            historyTask.remove(0);
            Task taskForArray = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getId());
            historyTask.add(taskForArray);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        return historyTask;
    }
}
