package Service;

import Service.Interface.HistoryManager;
import Service.InMemoryManager.InMemoryHistoryManager;
import Service.InMemoryManager.InMemoryTaskManager;
import Service.Interface.TaskManager;

public class Manager{
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
