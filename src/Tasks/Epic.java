package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static Tasks.TasksType.EPIC;

public class Epic extends Task {
    private transient final HashMap<Integer, SubTask> subTaskId = new HashMap<>();
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

    public void setStartDate(){
        if (!getSubTaskId().isEmpty()) {
            LocalDateTime StartEpic = LocalDateTime.of(4000, 01, 01, 00, 00);
            LocalDateTime EndEpic = LocalDateTime.of(1000, 01, 01, 00, 00);
            for (SubTask subTask : subTaskId.values()) {
                if (subTask.getStartDate() != null) {
                    LocalDateTime startDateTime = subTask.getStartDate();
                    if (startDateTime.isBefore(StartEpic)) {
                        StartEpic = startDateTime;
                    }
                    LocalDateTime endDateTime = subTask.getEndDate();
                    if (endDateTime.isAfter(EndEpic)) {
                        EndEpic = endDateTime;
                    }

                }
            }
            startDate = StartEpic;
            endDate = EndEpic;
        } else {
            startDate = null;
            endDate = null;
        }
    }

    public LocalDateTime getEndDate(){
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate){
        this.endDate = endDate;
    }



    public void setDuration(){
        duration = Duration.ofMinutes(0);
        for(SubTask subTask1 : subTaskId.values()){
            if(subTask1.getDuration() != null){
                duration = Duration.ofMinutes(duration.toMinutes() + subTask1.getDuration().toMinutes()) ;
            }
        }
    }

    public void setSubTaskId(int id, SubTask subTask){

        subTaskId.put(id, subTask);
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

    public Set<Integer> getSubTaskId() {
        return subTaskId.keySet();
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
