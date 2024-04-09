package Tasks;

import java.time.Duration;
import java.util.Objects;
import java.time.LocalDateTime;

import static Tasks.TasksType.TASK;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected String status;
    protected TasksType type;
    protected LocalDateTime startDate;
    protected Duration duration;


    public Task(String name, String description, String status, int id){
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = TASK;
    }
    public Task(String name, String description, String status, LocalDateTime startDate, long duration, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = TASK;
        this.startDate = startDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String description, LocalDateTime startDate, long duration) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TASK;
        this.startDate = startDate;
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String description, LocalDateTime startDate, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TASK;
        this.startDate = startDate;
        this.duration = duration;
    }

    public Task(String name, String description, long duration) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TASK;
        this.startDate = LocalDateTime.now();
        this.duration = Duration.ofMinutes(duration);
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = "NEW";
        this.type = TASK;
    }

    public Task(String name, String description, String status, LocalDateTime startDate, Duration duration, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = TASK;
        this.startDate = startDate;
        this.duration = duration;
    }

    public Task(String name, String description, String status, LocalDateTime startData, Long duration, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = TASK;
        this.startDate = startData;
        if(duration == null){
            this.duration = null;
        } else {
            this.duration = Duration.ofMinutes(duration);
        }
    }

    public LocalDateTime getEndDate() {
        if (startDate != null) {
            return startDate.plusMinutes(duration.toMinutes());
        } else {
            return null;
        }
    }

    public LocalDateTime getStartTime(){
        return startDate;
    }
    public void setStartTime(LocalDateTime startTime1){
        startDate = startTime1;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Duration getDuration() {
        return duration;
    }
    public String getName(){
        return name;
    }
    
    public TasksType getType(){
        return type;
    }
    
    public Integer getEpicId(){
        return null;
    }

    public String getDescription(){
        return description;
    }

    public String getStatus(){
        return status;
    }

    public int getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setId(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        return "name=" + name + '\'' +
                "description=" + description + '\'' +
                "status=" + status + '\'' +
                "id=" + id + '\'' +
                "type:" + TASK + '\'' +
                "startDate=" + startDate + '\'' +
                "endDate=" + getEndDate() + '\'' +
                "duration=" + duration + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
