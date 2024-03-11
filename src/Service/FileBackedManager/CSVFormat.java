package Service.FileBackedManager;

import Service.Interface.HistoryManager;
import Tasks.*;

import java.util.ArrayList;
import java.util.List;

import static Tasks.TasksType.EPIC;
import static Tasks.TasksType.TASK;

public class CSVFormat {
    private static final String HEAD = "id,type,name,status,description,epic";
    private static final String SPLITTER = ",";

    public static String toString(Task task){
        String line = task.getId() +
                SPLITTER + task.getType() +
                SPLITTER + task.getName() +
                SPLITTER + task.getStatus() +
                SPLITTER + task.getDescription() +
                SPLITTER + task.getEpicId();
        return line;
    }

    public static Task fromString(String value) {
        String[] lineContents = value.split(",");
        TasksType type = TasksType.valueOf(lineContents[1]);
        int id = Integer.parseInt(lineContents[0]);
        String name = lineContents[2];
        String status = lineContents[3];
        String description = lineContents[4];
        if (type == TASK) {
            return new Task(name, description, status, id);
        } else if (type == EPIC) {
            return new Epic(name, description, status, id);
        } else {
            int epicId = Integer.parseInt(lineContents[5]);
            return new SubTask(name, description, status, epicId, id);
        }
    }
    public static String historyToString(HistoryManager manager) {
        StringBuilder sB = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sB.append(task.getId());
            sB.append(SPLITTER);
        }
        if (!sB.isEmpty()) {
            sB.deleteCharAt(sB.length() - 1);
        }
        return sB.toString();
    }

    public static List<Integer> historyFromString(String str) {
        List<Integer> list = new ArrayList<>();
        if(str != null){
            String[] split = str.split(",");
            if (str.isEmpty()) {
                return list;
            }
            for (String i : split) {
                list.add(Integer.parseInt(i));
            }
        }

        return list;
    }

    public static String getHead() {
        return HEAD;
    }
}
