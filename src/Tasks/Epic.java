package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskId = new ArrayList<>();
    protected TreeSet<SubTask> prioritizedSubTasksForStart = new TreeSet<SubTask>(new ComparatorTaskForStart());
    protected TreeSet<SubTask> prioritizedSubTasksForEnd = new TreeSet<SubTask>(new ComparatorTaskForEnd());
    protected LocalDateTime endDate;
    protected TasksType type;





    public Epic(String name, String description) {
        super(name, description);
        this.startDate = null;
        this.type = TasksType.EPIC;
    }

    public Epic(String name, String description, String taskStatus, int id) {
        super(name, description, taskStatus, id);
        this.type = TasksType.EPIC;
    }

    public Epic(String name, String description, String status, LocalDateTime dateTime, Duration duration, int id) {
        super(name, description, status, dateTime, duration, id);
        this.type = TasksType.EPIC;
    }

    public Epic(String name, String description, String status, LocalDateTime startData, Long duration, int id) {
        super(name, description, status, startData,duration,id);
        this.type = TasksType.EPIC;
    }

    public void setStartDate(){
        startDate = prioritizedSubTasksForStart.getFirst().getStartDate();
    }

    public LocalDateTime getEndDate(){
        return endDate;
    }

    public void setEndDate(){
        endDate = prioritizedSubTasksForEnd.getLast().getEndDate();
    }

    public void setDuration(){
        duration = Duration.ofMinutes(0);
        for(SubTask subTask1 : prioritizedSubTasksForStart){
            if(subTask1.getDuration() != null){
                duration = Duration.ofMinutes(duration.toMinutes() + subTask1.getDuration().toMinutes()) ;
            }
        }
    }

    public void setSubTaskId(int id){

        subTaskId.add(id);
    }
    public void setSubTaskIdForTree(SubTask subTask){

        prioritizedSubTasksForStart.add(subTask);
        prioritizedSubTasksForEnd.add(subTask);
    }
    public TasksType getType(){
        return TasksType.EPIC;
    }

    public void deleteSubTaskId(Integer id){
        subTaskId.remove(id);
    }

    public void clearSubTaskId(){
        subTaskId.clear();
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    class ComparatorTaskForStart implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.equals(task2)) {
                return 0;
            } else if (task1.getStartDate() == null) {
                return 1;
            } else if (task2.getStartDate() == null) {
                return -1;
            } else if (task1.getStartDate().isBefore(task2.getStartDate())) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    class ComparatorTaskForEnd implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.equals(task2)) {
                return 0;
            } else if (task1.getEndDate() == null) {
                return 1;
            } else if (task2.getEndDate() == null) {
                return -1;
            } else if (task1.getEndDate().isBefore(task2.getEndDate())) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
