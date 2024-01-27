package Service.Interface;

import java.util.ArrayList;
import java.util.HashMap;
import Tasks.Task;

public interface HistoryManager {
    public void add(Task task);

    ArrayList<Task> getHistory();

}
