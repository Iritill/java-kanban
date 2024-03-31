package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public class SubTask extends Task{
    private int epicId;

    protected TasksType type;

    public SubTask(String name, String description, Epic epic) {
        super(name, description);
        this.status = "NEW";
        this.epicId = epic.getId();
        this.type = TasksType.SUBTASK;
    }

    public SubTask(String name, String description, String status, int id, Epic epic) {
        super(name, description, status, id);
        this.epicId = epic.getId();
        this.type = TasksType.SUBTASK;
    }

    public SubTask(String name, String description, String status, LocalDateTime startData, Long duration, int epicId, int id) {
        super(name, description, status, startData, duration, id);
        this.epicId = epicId;
        this.type = TasksType.SUBTASK;
    }
    public SubTask(String name, String description, LocalDateTime startDate, Long duration,  int epicId) {
        super(name, description, startDate, duration);
        this.epicId = epicId;
        this.type = TasksType.SUBTASK;
    }

    public SubTask(String name, String description, Long duration,  int epicId) {
        super(name, description, duration);
        this.startDate = LocalDateTime.now();
        this.epicId = epicId;
        this.type = TasksType.SUBTASK;
    }

    public Integer getEpicId(){
         return epicId;
    }

    public TasksType getType(){
        return TasksType.SUBTASK;
    }


}


