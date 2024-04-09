package Service.Interface;

import java.util.List;
import Tasks.Task;

public interface HistoryManager {
     public void add(Task task);
     public void remove(int id);
     List<Task> getHistory();

}
