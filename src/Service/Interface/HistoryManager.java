package Service.Interface;

import java.util.List;
import java.util.HashMap;
import Tasks.Task;

public interface HistoryManager {
     public void add(Task task);
     public void remove(int id);
     List<Task> getHistory();

}
