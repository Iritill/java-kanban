package Service;

import Service.FileBackedManager.FileBackedTaskManager;
import Service.Interface.HistoryManager;
import Service.InMemoryManager.InMemoryHistoryManager;
import Service.InMemoryManager.InMemoryTaskManager;
import Service.Interface.TaskManager;

import java.io.File;

public class Manager{
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBackedTaskManager(File file){return new FileBackedTaskManager(file);}
}
