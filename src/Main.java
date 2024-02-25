import Service.InMemoryManager.*;
import Service.Interface.TaskManager;
import Service.Manager;
import Tasks.*;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args){
        InMemoryTaskManager manager = (InMemoryTaskManager) Manager.getDefault();

        int task1 = manager.create(new Task("1313", "312313"));
        Task tasks = manager.getTask(task1);

        System.out.println(manager.getHistory());
    }
}


