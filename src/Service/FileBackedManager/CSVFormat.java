package Service.FileBackedManager;

import Service.Interface.HistoryManager;
import Tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static Tasks.TasksType.EPIC;
import static Tasks.TasksType.TASK;

public class CSVFormat {
    private static final String HEAD = "id,type,name,status,description,epic,startDate,endDate,duration";
    private static final String SPLITTER = ",";

    public static String toString(Task task){
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss - SSSSSSSSS");
        String DateTime;
        String endDate;
        if (task.getStartDate() == null) {
            DateTime = "null";
        } else {
            DateTime = task.getStartDate().format(inputFormatter);
        } if(task.getEndDate() == null){
            endDate = "null";
        } else{
            endDate = task.getEndDate().format(inputFormatter);
        }
        String line = task.getId() +
                SPLITTER + task.getType() +
                SPLITTER + task.getName() +
                SPLITTER + task.getStatus() +
                SPLITTER + task.getDescription() +
                SPLITTER + task.getEpicId() +
                SPLITTER + DateTime +
                SPLITTER + endDate +
                SPLITTER + task.getDuration();

        return line;
    }

    public static Task fromString(String value) {
        String[] lineContents = value.split(",");
        TasksType type = TasksType.valueOf(lineContents[1]);
        int id = Integer.parseInt(lineContents[0]);
        String name = lineContents[2];
        String status = lineContents[3];
        String description = lineContents[4];
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm.ss - SSSSSSSSS");
        LocalDateTime startData;
        LocalDateTime endData;
        if (lineContents[6].equals("null")) {
            startData = null;

        } else {
            startData = LocalDateTime.parse(lineContents[6], inputFormatter);
        }

        if(lineContents[7].equals("null")){
            endData = null;
        } else {
            endData = LocalDateTime.parse(lineContents[6], inputFormatter);
        }
        Duration duration;
        if(lineContents[8].equals("null") || lineContents[8].equals("PT0S")){
            duration = null;
            if (type == TASK) {
                return new Task(name, description, status, startData, (Duration) null, id);
            } else if (type == EPIC) {
                return new Epic(name, description, status, startData, (Duration) null, id);
            } else {
                int epicId = Integer.parseInt(lineContents[5]);
                return new SubTask(name, description, status, startData, (Long) null, epicId, id);
            }
        } else {
            //duration = Duration.between(LocalTime.MIN, LocalTime.parse(lineContents[8]));
            duration = Duration.parse(lineContents[8]);
            if (type == TASK) {
                return new Task(name, description, status, startData, duration.toMinutes(), id);
            } else if (type == EPIC) {
                return new Epic(name, description, status, startData, duration.toMinutes(), id);
            } else {
                int epicId = Integer.parseInt(lineContents[5]);
                return new SubTask(name, description, status, startData, duration.toMinutes(), epicId, id);
            }
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
