package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static Tasks.TasksType.EPIC;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();
    protected LocalDateTime endDate;


    public Epic(String name, String description) {
        super(name, description);
        this.startDate = null;
        this.type = EPIC;
    }

    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.type = EPIC;
    }

    public Epic(String name, String description, String status, LocalDateTime dateTime, Duration duration, int id) {
        super(name, description, status, dateTime, duration, id);
        this.type = EPIC;
    }

    public Epic(String name, String description, String status, LocalDateTime startData, Long duration, int id) {
        super(name, description, status, startData,duration,id);
        this.type = EPIC;
    }

    public LocalDateTime getEndDate(){
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate){
        this.endDate = endDate;
    }

    public void setStartDate(LocalDateTime startDate){
        this.startDate = startDate;
    }



    public void setDuration(Duration duration){
        this.duration = duration;
    }

    public void setSubTaskId(int id){

        subTaskId.add(id);
    }
    public TasksType getType(){
        return EPIC;
    }

    public void deleteSubTaskId(Integer id){
        subTaskId.remove(id);
    }

    public void clearSubTaskId(){
        subTaskId.clear();
    }

    public List<Integer> getSubTaskId() {
        return subTaskId;
    }

    @Override
    public String toString(){
        return "name=" + name + '\'' +
                "description=" + description + '\'' +
                "status=" + status + '\'' +
                "id=" + id + '\'' +
                "type:" + EPIC + '\'' +
                "startDate=" + startDate + '\'' +
                "endDate=" + getEndDate() + '\'' +
                "duration=" + duration + '\'';
    }
}
